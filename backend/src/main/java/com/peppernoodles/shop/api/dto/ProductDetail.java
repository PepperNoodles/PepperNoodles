package com.peppernoodles.shop.api.dto;

import com.peppernoodles.restaurant.api.dto.TagSummary;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductDetail(
        Long id, String name, String description, BigDecimal price, int quantity,
        String imageUrl, String status, Long restaurantId, String restaurantName,
        String categoryName, String subcategoryName, List<TagSummary> tags,
        boolean editable, Instant releasedAt) {}
