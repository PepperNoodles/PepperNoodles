package com.peppernoodles.shop.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record SaveProductRequest(
        @NotNull Long restaurantId,
        Long subcategoryId,
        @NotBlank @Size(max = 100) String name,
        @Size(max = 2000) String description,
        @NotNull @DecimalMin(value = "0.0", message = "價格不可為負數") BigDecimal price,
        @Min(value = 0, message = "庫存不可為負數") int quantity,
        List<Long> tagIds,
        boolean listed) {}
