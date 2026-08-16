package com.peppernoodles.map.api.dto;

import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** The map viewport, for drawing markers as the user pans. */
public record MapBoundsRequest(
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double south,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double west,
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double north,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double east,
        @Min(1) @Max(500) Integer limit) {

    public int limitOrDefault() {
        return limit == null ? 200 : limit;
    }

    /**
     * Rejects an inverted box. Bean validation checks each corner independently
     * and cannot express the relationship between them.
     */
    public void validateOrdering() {
        if (north <= south) {
            throw new ValidationException("north must be greater than south.");
        }
        if (east <= west) {
            // A viewport crossing the antimeridian would arrive inverted. The
            // frontend splits such a viewport into two requests rather than
            // having the query handle the wrap.
            throw new ValidationException("east must be greater than west.");
        }
    }
}
