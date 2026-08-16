package com.peppernoodles.auth.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** 企業會員註冊. Creates an account holding {@code ROLE_COMPANY}. */
public record RegisterCompanyRequest(
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank
                @Size(min = 8, max = 100, message = "密碼長度需介於 8 到 100 個字元")
                @Pattern(
                        regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                        message = "密碼需同時包含英文字母與數字")
                String password,
        @NotBlank @Size(max = 100) String companyName,
        @Pattern(regexp = "^0\\d{1,2}-?\\d{6,8}$", message = "請輸入有效的市話或手機號碼") String phone,
        @Size(max = 200) String location,
        String recaptchaToken) {}
