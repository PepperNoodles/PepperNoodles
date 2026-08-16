package com.peppernoodles.restaurant.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveReplyRequest(
        @NotBlank @Size(max = 1000, message = "回覆長度不可超過 1000 字") String body) {}
