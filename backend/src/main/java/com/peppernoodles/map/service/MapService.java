package com.peppernoodles.map.service;

import com.peppernoodles.map.api.dto.MapBoundsRequest;
import com.peppernoodles.map.api.dto.MapSearchRequest;
import com.peppernoodles.map.repository.RestaurantSearchRepository;
import com.peppernoodles.map.repository.RestaurantSearchRepository.NearbyRestaurantRow;
import com.peppernoodles.restaurant.api.dto.RatingSummary;
import com.peppernoodles.restaurant.api.dto.RestaurantSummary;
import com.peppernoodles.restaurant.api.dto.TagSummary;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 地圖搜尋.
 *
 * <p>Tags are attached in a single follow-up query keyed by restaurant id rather
 * than by navigating each entity, so a viewport with 200 markers costs two
 * queries instead of 201.
 */
@Service
public class MapService {

    private final RestaurantSearchRepository search;
    private final RestaurantRepository restaurants;
    private final StorageService storage;

    public MapService(
            RestaurantSearchRepository search, RestaurantRepository restaurants, StorageService storage) {
        this.search = search;
        this.restaurants = restaurants;
        this.storage = storage;
    }

    @Transactional(readOnly = true)
    public List<RestaurantSummary> nearby(MapSearchRequest request) {
        return toSummaries(search.findNearby(
                request.latitude(), request.longitude(), request.radiusOrDefault(), request.limitOrDefault()));
    }

    @Transactional(readOnly = true)
    public List<RestaurantSummary> withinBounds(MapBoundsRequest request) {
        request.validateOrdering();
        return toSummaries(search.findWithinBounds(
                request.south(), request.west(), request.north(), request.east(), request.limitOrDefault()));
    }

    private List<RestaurantSummary> toSummaries(List<NearbyRestaurantRow> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }

        List<Long> ids = rows.stream().map(NearbyRestaurantRow::getId).toList();
        Map<Long, List<TagSummary>> tagsById = restaurants.findAllById(ids).stream()
                .collect(Collectors.toMap(
                        r -> r.getId(),
                        r -> r.getFoodTags().stream().map(TagSummary::from).toList()));

        return rows.stream()
                .map(row -> new RestaurantSummary(
                        row.getId(),
                        row.getName(),
                        row.getAddress(),
                        row.getContact(),
                        row.getWebsite(),
                        storage.publicUrl(StorageBucket.RESTAURANT_PHOTOS, row.getPhotoPath()),
                        row.getLatitude(),
                        row.getLongitude(),
                        new RatingSummary(
                                row.getRatingAverage(),
                                row.getRatingCount() == null ? 0 : row.getRatingCount(),
                                row.getReviewCount() == null ? 0 : row.getReviewCount()),
                        tagsById.getOrDefault(row.getId(), List.of()),
                        row.getDistanceMetres()))
                .toList();
    }
}
