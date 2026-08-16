package com.peppernoodles.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank String currentPassword,
        @NotBlank
                @Size(min = 8, max = 100, message = "密碼長度需介於 8 到 100 個字元")
                @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "密碼需同時包含英文字母與數字")
                String newPassword) {}
