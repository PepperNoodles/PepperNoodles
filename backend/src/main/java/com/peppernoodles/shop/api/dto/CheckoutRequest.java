package com.peppernoodles.shop.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** 結帳 — turns the caller's cart into an order. */
public record CheckoutRequest(
        @NotBlank @Size(max = 50) String receiverName,
        @NotBlank @Pattern(regexp = "^09\\d{8}$", message = "請輸入有效的收件人手機號碼") String receiverPhone,
        @NotBlank @Size(max = 200) String receiverAddress) {}
