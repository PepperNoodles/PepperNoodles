package com.peppernoodles.shop.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequest(
        @NotNull Long productId,
        @Min(value = 1, message = "數量至少為 1") @Max(value = 999, message = "數量上限為 999") int quantity) {}
