package com.peppernoodles.restaurant.api.dto;

import java.math.BigDecimal;
import java.util.List;

/** List-view shape: enough for a card or a map marker, nothing more. */
public record RestaurantSummary(
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
        /** Populated only by proximity search; null elsewhere. */
        Double distanceMetres) {}
