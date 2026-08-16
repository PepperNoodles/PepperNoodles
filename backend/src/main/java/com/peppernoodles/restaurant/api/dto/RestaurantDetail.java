package com.peppernoodles.restaurant.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/** Full restaurant page. */
public record RestaurantDetail(
        Long id,
        String name,
        String address,
        String contact,
        String website,
        String photoUrl,
        BigDecimal latitude,
        BigDecimal longitude,
        RatingSummary rating,
        List<TagSummary> tags,
        List<BusinessHourDto> businessHours,
        List<MenuItemDto> menu,
        List<RestaurantEventDto> activeEvents,
        OwnerSummary owner,
        /** Whether the caller has this restaurant in their favourites; false when anonymous. */
        boolean favourited,
        /** Whether the caller may edit it. */
        boolean editable,
        Instant createdAt) {

    public record OwnerSummary(Long userId, String name) {}
}
