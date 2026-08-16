package com.peppernoodles.shop.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDto(
        Long id, String orderNo, String status, BigDecimal totalCost,
        String receiverName, String receiverPhone, String receiverAddress,
        Instant createdAt, Instant paidAt, Instant expiresAt,
        List<OrderLine> items) {

    public record OrderLine(
            Long productId, String productName, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {}
}
