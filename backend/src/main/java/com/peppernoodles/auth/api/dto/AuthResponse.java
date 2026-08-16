package com.peppernoodles.auth.api.dto;

import java.time.Instant;
import java.util.List;

/**
 * Issued on login and on refresh.
 *
 * <p>The refresh token is returned in the body rather than a cookie because the
 * frontend is a separate origin; the client stores it and sends it back to
 * {@code POST /api/v1/auth/refresh}.
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        Instant expiresAt,
        AuthenticatedUserSummary user) {

    public static AuthResponse of(
            String accessToken,
            String refreshToken,
            long expiresIn,
            Instant expiresAt,
            AuthenticatedUserSummary user) {
        return new AuthResponse(accessToken, refreshToken, "Bearer", expiresIn, expiresAt, user);
    }

    /** Just enough for the client to render the header and route by role. */
    public record AuthenticatedUserSummary(
            Long id, String email, String displayName, String avatarPath, List<String> roles) {}
}
