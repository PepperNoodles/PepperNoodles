package com.peppernoodles.restaurant.api.dto;

import java.time.Instant;
import java.time.LocalDate;

/** Landing-page rollups. */
public final class DiscoveryDtos {

    private DiscoveryDtos() {}

    /** 熱門地點 */
    public record DistrictDto(String district, long restaurantCount) {}

    /** 美食優惠 */
    public record CampaignDto(
            Long id, String name, String content, String imageUrl,
            LocalDate startsOn, LocalDate endsOn, Long restaurantId, String restaurantName) {}

    /** 客戶評論 */
    public record HighlightReviewDto(
            Long id, String body, Integer score, Instant createdAt,
            Long restaurantId, String restaurantName, String authorName, String authorAvatarUrl) {}
}
