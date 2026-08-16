package com.peppernoodles.shop.api.dto;

import com.peppernoodles.restaurant.api.dto.TagSummary;
import java.math.BigDecimal;
import java.util.List;

public record ProductSummary(
        Long id, String name, BigDecimal price, int quantity, String imageUrl,
        String status, Long restaurantId, String restaurantName,
        String categoryName, String subcategoryName, List<TagSummary> tags) {}
