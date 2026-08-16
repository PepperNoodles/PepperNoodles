package com.peppernoodles.restaurant.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Create or update a restaurant.
 *
 * <p>The legacy flow validated the address in JavaScript and geocoded it in the
 * controller, silently storing (0, 0) when lookup failed. Coordinates are
 * required here and range-checked by both bean validation and a database CHECK.
 */
public record SaveRestaurantRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 200) String address,
        @Pattern(regexp = "^[0-9+\\-() ]{6,20}$", message = "請輸入有效的聯絡電話") String contact,
        @Size(max = 300) String website,
        @NotNull
                @DecimalMin(value = "-90.0", message = "緯度需介於 -90 到 90")
                @DecimalMax(value = "90.0", message = "緯度需介於 -90 到 90")
                BigDecimal latitude,
        @NotNull
                @DecimalMin(value = "-180.0", message = "經度需介於 -180 到 180")
                @DecimalMax(value = "180.0", message = "經度需介於 -180 到 180")
                BigDecimal longitude,
        List<Long> tagIds,
        @Valid List<BusinessHourDto> businessHours) {}
