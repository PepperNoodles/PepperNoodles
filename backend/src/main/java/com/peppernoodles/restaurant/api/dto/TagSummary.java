package com.peppernoodles.restaurant.api.dto;

import com.peppernoodles.tag.domain.FoodTag;

public record TagSummary(Long id, String name) {

    public static TagSummary from(FoodTag tag) {
        return new TagSummary(tag.getId(), tag.getName());
    }
}
