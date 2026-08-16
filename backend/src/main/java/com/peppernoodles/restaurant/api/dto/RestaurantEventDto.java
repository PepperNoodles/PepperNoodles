package com.peppernoodles.restaurant.api.dto;

import java.time.LocalDate;

public record RestaurantEventDto(
        Long id, Long restaurantId, String name, String content, String imageUrl,
        LocalDate startsOn, LocalDate endsOn, boolean active) {}
