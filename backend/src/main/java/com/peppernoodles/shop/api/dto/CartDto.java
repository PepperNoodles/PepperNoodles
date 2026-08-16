package com.peppernoodles.shop.api.dto;

import java.math.BigDecimal;
import java.util.List;

/** 購物車. */
public record CartDto(List<CartLine> items, BigDecimal total, boolean hasUnavailableItems) {

    public record CartLine(
            Long productId, String name, String imageUrl, BigDecimal unitPrice,
            int quantity, BigDecimal lineTotal, int availableStock,
            boolean unavailable) {}
}
