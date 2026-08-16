package com.peppernoodles.restaurant.api.dto;

import java.time.Instant;
import java.util.List;

public record ReviewDto(
        Long id,
        AuthorSummary author,
        String body,
        Short score,
        Instant createdAt,
        List<ReplyDto> replies,
        /** True when the caller wrote this review and may edit or delete it. */
        boolean editable) {

    public record AuthorSummary(Long userId, String displayName, String avatarUrl) {}

    public record ReplyDto(
            Long id, AuthorSummary author, String body, Instant createdAt,
            boolean fromRestaurantOwner, boolean editable) {}
}
