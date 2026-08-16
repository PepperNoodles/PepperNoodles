package com.peppernoodles.restaurant.api.dto;

import com.peppernoodles.restaurant.domain.BusinessHour;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

/** One opening interval. dayOfWeek is 0 = Sunday … 6 = Saturday. */
public record BusinessHourDto(
        Long id,
        @NotNull @Min(0) @Max(6) Short dayOfWeek,
        @NotNull LocalTime opensAt,
        @NotNull LocalTime closesAt) {

    public static BusinessHourDto from(BusinessHour hour) {
        return new BusinessHourDto(hour.getId(), hour.getDayOfWeek(), hour.getOpensAt(), hour.getClosesAt());
    }
}
