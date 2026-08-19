package com.peppernoodles.admin.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

/** Request and response shapes for the 後台. */
public final class AdminDtos {

    private AdminDtos() {}

    /** 聯絡我們 submission — reachable while logged out. */
    public record CreateInquiryRequest(
            @Email String contactEmail,
            @NotBlank @Size(max = 2000, message = "訊息長度不可超過 2000 字") String body,
            String recaptchaToken) {}

    public record InquiryDto(
            Long id,
            Long userId,
            String submitterName,
            String contactEmail,
            String body,
            String status,
            String resolutionNote,
            Instant createdAt,
            Instant resolvedAt) {}

    public record ResolveInquiryRequest(@Size(max = 1000) String resolutionNote) {}

    public record SuspendUserRequest(
            @NotBlank @Size(max = 500, message = "停權原因不可超過 500 字") String reason) {}

    /** One row of the member list. */
    public record ManagedUserDto(
            Long id,
            String email,
            String displayName,
            List<String> roles,
            boolean enabled,
            boolean suspended,
            String suspendedReason,
            Instant lastLoginAt,
            Instant createdAt) {}

    public record AuditLogDto(
            Long id, Long actorUserId, String actorEmail, String action,
            String targetType, String targetId, String detail, Instant createdAt) {}

    /** One row of the back-office restaurant list. */
    public record ManagedRestaurantDto(
            Long id,
            String name,
            String address,
            String contact,
            Long ownerUserId,
            String ownerEmail,
            String ownerName,
            long reviewCount,
            java.math.BigDecimal ratingAverage,
            java.time.Instant createdAt) {}

    /** 後台首頁 counters. */
    public record DashboardDto(
            long totalUsers, long suspendedUsers, long totalRestaurants,
            long totalProducts, long pendingOrders, long openInquiries) {}
}
