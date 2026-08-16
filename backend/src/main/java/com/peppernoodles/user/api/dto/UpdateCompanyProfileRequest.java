package com.peppernoodles.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCompanyProfileRequest(
        @NotBlank @Size(max = 100) String companyName,
        @Pattern(regexp = "^0\\d{1,2}-?\\d{6,8}$", message = "請輸入有效的市話或手機號碼") String phone,
        @Size(max = 200) String location) {}
