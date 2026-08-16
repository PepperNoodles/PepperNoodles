package com.peppernoodles.restaurant.service;

import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.restaurant.api.dto.BusinessHourDto;
import com.peppernoodles.restaurant.api.dto.RatingSummary;
import com.peppernoodles.restaurant.api.dto.RestaurantDetail;
import com.peppernoodles.restaurant.api.dto.RestaurantSummary;
import com.peppernoodles.restaurant.api.dto.SaveRestaurantRequest;
import com.peppernoodles.restaurant.domain.BusinessHour;
import com.peppernoodles.restaurant.domain.MenuItem;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.repository.FavouriteRestaurantRepository;
import com.peppernoodles.restaurant.repository.MenuItemRepository;
import com.peppernoodles.restaurant.repository.RestaurantEventRepository;
import com.peppernoodles.restaurant.repository.RestaurantRatingRepository;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import com.peppernoodles.tag.repository.FoodTagRepository;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/** Restaurant browsing and owner-side CRUD. */
@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurants;
    private final RestaurantRatingRepository ratings;
    private final RestaurantEventRepository events;
    private final MenuItemRepository menuItems;
    private final FavouriteRestaurantRepository favourites;
    private final FoodTagRepository foodTags;
    private final UserRepository users;
    private final StorageService storage;
    private final RestaurantMapper mapper;

    public RestaurantService(
            RestaurantRepository restaurants,
            RestaurantRatingRepository ratings,
            RestaurantEventRepository events,
            MenuItemRepository menuItems,
            FavouriteRestaurantRepository favourites,
            FoodTagRepository foodTags,
            UserRepository users,
            StorageService storage,
            RestaurantMapper mapper) {
        this.restaurants = restaurants;
        this.ratings = ratings;
        this.events = events;
        this.menuItems = menuItems;
        this.favourites = favourites;
        this.foodTags = foodTags;
        this.users = users;
        this.storage = storage;
        this.mapper = mapper;
    }

    // --- reads ---------------------------------------------------------------

    @Transactional(readOnly = true)
    public PageResponse<RestaurantSummary> list(String query, List<Long> tagIds, Pageable pageable) {
        Page<Restaurant> page;
        if (StringUtils.hasText(query)) {
            page = restaurants.searchByNameOrAddress(query.trim(), pageable);
        } else if (tagIds != null && !tagIds.isEmpty()) {
            page = restaurants.findByAnyTag(tagIds, pageable);
        } else {
            page = restaurants.findAll(pageable);
        }
        return toSummaryPage(page);
    }

    @Transactional(readOnly = true)
    public PageResponse<RestaurantSummary> listOwnedBy(Long ownerId, Pageable pageable) {
        return toSummaryPage(restaurants.findByOwnerId(ownerId, pageable));
    }

    @Transactional(readOnly = true)
    public PageResponse<RestaurantSummary> listFavouritesOf(Long userId, Pageable pageable) {
        return toSummaryPage(favourites.findFavouritesOf(userId, pageable));
    }

    @Transactional(readOnly = true)
    public RestaurantDetail get(Long id, AuthenticatedUser caller) {
        Restaurant restaurant = restaurants.findDetailedById(id).orElseThrow(() -> NotFoundException.of("餐廳", id));

        RatingSummary rating = RestaurantMapper.toRating(ratings.findRatingFor(id).orElse(null));
        Long callerId = caller == null ? null : caller.id();

        return new RestaurantDetail(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getContact(),
                restaurant.getWebsite(),
                mapper.photoUrl(restaurant.getPhotoPath()),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                rating,
                mapper.toTags(restaurant),
                mapper.toBusinessHours(restaurant),
                menuItems.findByRestaurantIdOrderByPositionAscIdAsc(id).stream()
                        .map(mapper::toMenuItem)
                        .toList(),
                events.findActive(id, LocalDate.now()).stream().map(mapper::toEvent).toList(),
                new RestaurantDetail.OwnerSummary(
                        restaurant.getOwner().getId(), ownerDisplayName(restaurant.getOwner())),
                callerId != null && favourites.isFavourite(callerId, id),
                canEdit(restaurant, caller),
                restaurant.getCreatedAt());
    }

    // --- writes --------------------------------------------------------------

    @Transactional
    public Long create(SaveRestaurantRequest request, AuthenticatedUser caller) {
        if (restaurants.existsByAddress(request.address())) {
            throw new ConflictException("這個地址已經登記過餐廳了。");
        }

        User owner = users.findById(caller.id()).orElseThrow(() -> NotFoundException.of("使用者", caller.id()));

        Restaurant restaurant = new Restaurant(
                owner, request.name(), request.address(), request.latitude(), request.longitude());
        restaurant.setContact(request.contact());
        restaurant.setWebsite(request.website());
        applyTags(restaurant, request.tagIds());
        applyBusinessHours(restaurant, request.businessHours());

        Long id = restaurants.save(restaurant).getId();
        log.info("User {} created restaurant {}", caller.id(), id);
        return id;
    }

    @Transactional
    public void update(Long id, SaveRestaurantRequest request, AuthenticatedUser caller) {
        Restaurant restaurant = requireEditable(id, caller);

        if (restaurants.existsByAddressAndIdNot(request.address(), id)) {
            throw new ConflictException("這個地址已經登記過餐廳了。");
        }

        restaurant.setName(request.name());
        restaurant.setAddress(request.address());
        restaurant.setContact(request.contact());
        restaurant.setWebsite(request.website());
        restaurant.setLatitude(request.latitude());
        restaurant.setLongitude(request.longitude());
        applyTags(restaurant, request.tagIds());
        applyBusinessHours(restaurant, request.businessHours());
    }

    @Transactional
    public void delete(Long id, AuthenticatedUser caller) {
        Restaurant restaurant = requireEditable(id, caller);
        String photoPath = restaurant.getPhotoPath();

        restaurants.delete(restaurant);
        storage.delete(StorageBucket.RESTAURANT_PHOTOS, photoPath);
        log.info("User {} deleted restaurant {}", caller.id(), id);
    }

    @Transactional
    public String uploadPhoto(Long id, MultipartFile file, AuthenticatedUser caller) {
        Restaurant restaurant = requireEditable(id, caller);
        String previous = restaurant.getPhotoPath();

        String path = storage.upload(StorageBucket.RESTAURANT_PHOTOS, file);
        restaurant.setPhotoPath(path);

        storage.delete(StorageBucket.RESTAURANT_PHOTOS, previous);
        return mapper.photoUrl(path);
    }

    @Transactional
    public void addMenuItem(Long id, MultipartFile file, String caption, AuthenticatedUser caller) {
        Restaurant restaurant = requireEditable(id, caller);
        String path = storage.upload(StorageBucket.MENU_PHOTOS, file);
        restaurant.addMenuItem(new MenuItem(path, caption, restaurant.getMenuItems().size()));
    }

    @Transactional
    public void deleteMenuItem(Long restaurantId, Long menuItemId, AuthenticatedUser caller) {
        Restaurant restaurant = requireEditable(restaurantId, caller);

        MenuItem item = menuItems
                .findById(menuItemId)
                .filter(m -> m.getRestaurant().getId().equals(restaurantId))
                .orElseThrow(() -> NotFoundException.of("菜單項目", menuItemId));

        restaurant.getMenuItems().remove(item);
        menuItems.delete(item);
        storage.delete(StorageBucket.MENU_PHOTOS, item.getImagePath());
    }

    @Transactional
    public void addFavourite(Long restaurantId, Long userId) {
        requireExists(restaurantId);
        favourites.addFavourite(userId, restaurantId);
    }

    @Transactional
    public void removeFavourite(Long restaurantId, Long userId) {
        favourites.removeFavourite(userId, restaurantId);
    }

    // --- helpers -------------------------------------------------------------

    /**
     * Loads a restaurant the caller is allowed to change: its owner, or an admin.
     *
     * <p>Central to authorization for this domain — the legacy controllers took
     * the acting user from a request parameter and never checked ownership at
     * all, so any logged-in user could edit any restaurant by guessing an id.
     */
    private Restaurant requireEditable(Long id, AuthenticatedUser caller) {
        Restaurant restaurant = restaurants.findById(id).orElseThrow(() -> NotFoundException.of("餐廳", id));
        if (!canEdit(restaurant, caller)) {
            throw new ForbiddenException("您沒有權限修改這間餐廳。");
        }
        return restaurant;
    }

    private void requireExists(Long id) {
        if (!restaurants.existsById(id)) {
            throw NotFoundException.of("餐廳", id);
        }
    }

    private static boolean canEdit(Restaurant restaurant, AuthenticatedUser caller) {
        return caller != null && (restaurant.isOwnedBy(caller.id()) || caller.isAdmin());
    }

    private void applyTags(Restaurant restaurant, List<Long> tagIds) {
        if (tagIds == null) {
            return;
        }
        restaurant.setFoodTags(new LinkedHashSet<>(foodTags.findByIdIn(tagIds)));
    }

    private void applyBusinessHours(Restaurant restaurant, List<BusinessHourDto> hours) {
        if (hours == null) {
            return;
        }
        restaurant.replaceBusinessHours(hours.stream()
                .map(h -> new BusinessHour(h.dayOfWeek(), h.opensAt(), h.closesAt()))
                .toList());
    }

    private static String ownerDisplayName(User owner) {
        if (owner.getCompanyProfile() != null) {
            return owner.getCompanyProfile().getRealName();
        }
        if (owner.getProfile() != null) {
            return owner.getProfile().getNickname();
        }
        return "店家";
    }

    /** Batch-loads ratings for the page so the list does not fire one query per row. */
    private PageResponse<RestaurantSummary> toSummaryPage(Page<Restaurant> page) {
        List<Long> ids = page.getContent().stream().map(Restaurant::getId).toList();
        Map<Long, RatingSummary> ratingsById = ids.isEmpty()
                ? Map.of()
                : ratings.findRatingsFor(ids).stream()
                        .collect(Collectors.toMap(
                                r -> r.getRestaurantId(), RestaurantMapper::toRating, (a, b) -> a));

        Function<Restaurant, RestaurantSummary> toSummary = restaurant ->
                mapper.toSummary(restaurant, ratingsById.getOrDefault(restaurant.getId(), RatingSummary.NONE), null);

        return PageResponse.of(page, toSummary);
    }
}
