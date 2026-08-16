package com.peppernoodles.map.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** 附近搜尋 — restaurants within a radius of a point. */
public record MapSearchRequest(
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
        /** Search radius in metres. Capped so a huge value cannot scan the table. */
        @Min(50) @Max(50_000) Integer radiusMetres,
        @Min(1) @Max(200) Integer limit) {

    public int radiusOrDefault() {
        return radiusMetres == null ? 1_000 : radiusMetres;
    }

    public int limitOrDefault() {
        return limit == null ? 50 : limit;
    }
}
