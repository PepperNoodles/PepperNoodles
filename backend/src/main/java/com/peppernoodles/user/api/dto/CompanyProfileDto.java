package com.peppernoodles.user.api.dto;

import java.time.Instant;

public record CompanyProfileDto(
        Long userId, String email, String companyName, String phone,
        String location, String tier, String avatarUrl, Instant createdAt) {}
