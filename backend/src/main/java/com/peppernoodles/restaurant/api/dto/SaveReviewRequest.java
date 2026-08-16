package com.peppernoodles.restaurant.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveReviewRequest(
        @NotBlank @Size(max = 2000, message = "評論長度不可超過 2000 字") String body,
        @Min(value = 1, message = "評分需介於 1 到 5") @Max(value = 5, message = "評分需介於 1 到 5") Short score) {}
