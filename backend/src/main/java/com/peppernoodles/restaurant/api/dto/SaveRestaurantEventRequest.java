package com.peppernoodles.restaurant.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record SaveRestaurantEventRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 2000) String content,
        @NotNull LocalDate startsOn,
        @NotNull LocalDate endsOn) {}
