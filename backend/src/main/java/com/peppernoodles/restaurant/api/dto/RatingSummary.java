package com.peppernoodles.restaurant.api.dto;

import java.math.BigDecimal;

/** Derived from the restaurant_ratings view; never stored on the restaurant row. */
public record RatingSummary(BigDecimal average, long ratingCount, long reviewCount) {

    public static final RatingSummary NONE = new RatingSummary(null, 0, 0);
}
