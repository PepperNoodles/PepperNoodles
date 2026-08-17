package com.peppernoodles.restaurant.service;

import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.restaurant.api.dto.RestaurantEventDto;
import com.peppernoodles.restaurant.api.dto.SaveRestaurantEventRequest;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.domain.RestaurantEvent;
import com.peppernoodles.restaurant.repository.RestaurantEventRepository;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/** 餐廳活動 management for the owning business account. */
@Service
public class RestaurantEventService {

    private final RestaurantEventRepository events;
    private final RestaurantRepository restaurants;
    private final StorageService storage;
    private final RestaurantMapper mapper;

    public RestaurantEventService(
            RestaurantEventRepository events,
            RestaurantRepository restaurants,
            StorageService storage,
            RestaurantMapper mapper) {
        this.events = events;
        this.restaurants = restaurants;
        this.storage = storage;
        this.mapper = mapper;
    }

    /** Every event for a restaurant, past and future — the owner's view. */
    @Transactional(readOnly = true)
    public List<RestaurantEventDto> listAll(Long restaurantId) {
        return events.findByRestaurantIdOrderByStartsOnDesc(restaurantId).stream()
                .map(mapper::toEvent)
                .toList();
    }

    @Transactional
    public Long create(Long restaurantId, SaveRestaurantEventRequest request, AuthenticatedUser caller) {
        Restaurant restaurant = requireEditable(restaurantId, caller);
        validateDates(request);

        RestaurantEvent event = new RestaurantEvent();
        event.setRestaurant(restaurant);
        apply(event, request);
        return events.save(event).getId();
    }

    @Transactional
    public void update(Long eventId, SaveRestaurantEventRequest request, AuthenticatedUser caller) {
        RestaurantEvent event = requireEditableEvent(eventId, caller);
        validateDates(request);
        apply(event, request);
    }

    @Transactional
    public void delete(Long eventId, AuthenticatedUser caller) {
        RestaurantEvent event = requireEditableEvent(eventId, caller);
        String image = event.getImagePath();
        events.delete(event);
        storage.delete(StorageBucket.EVENT_PHOTOS, image);
    }

    @Transactional
    public String uploadImage(Long eventId, MultipartFile file, AuthenticatedUser caller) {
        RestaurantEvent event = requireEditableEvent(eventId, caller);

        String previous = event.getImagePath();
        String path = storage.upload(StorageBucket.EVENT_PHOTOS, file);
        event.setImagePath(path);
        storage.delete(StorageBucket.EVENT_PHOTOS, previous);

        return storage.publicUrl(StorageBucket.EVENT_PHOTOS, path);
    }

    private static void apply(RestaurantEvent event, SaveRestaurantEventRequest request) {
        event.setName(request.name());
        event.setContent(request.content());
        event.setStartsOn(request.startsOn());
        event.setEndsOn(request.endsOn());
    }

    /**
     * The database CHECK also enforces this, but failing here produces a field
     * message the form can show instead of a generic constraint violation.
     */
    private static void validateDates(SaveRestaurantEventRequest request) {
        if (request.endsOn().isBefore(request.startsOn())) {
            throw new ValidationException("活動結束日期不可早於開始日期。");
        }
    }

    private Restaurant requireEditable(Long restaurantId, AuthenticatedUser caller) {
        Restaurant restaurant =
                restaurants.findById(restaurantId).orElseThrow(() -> NotFoundException.of("餐廳", restaurantId));
        if (caller == null || !(restaurant.isOwnedBy(caller.id()) || caller.isAdmin())) {
            throw new ForbiddenException("您沒有權限管理這間餐廳的活動。");
        }
        return restaurant;
    }

    private RestaurantEvent requireEditableEvent(Long eventId, AuthenticatedUser caller) {
        RestaurantEvent event = events.findById(eventId).orElseThrow(() -> NotFoundException.of("活動", eventId));
        requireEditable(event.getRestaurant().getId(), caller);
        return event;
    }
}
