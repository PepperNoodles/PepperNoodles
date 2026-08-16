package com.peppernoodles.common.security;

import java.util.Set;

/**
 * The caller behind the current request, projected from the access token.
 *
 * <p>Injected into controllers with {@link CurrentUser}. Controllers must take
 * the caller's identity from here and never from a request parameter — the
 * legacy code passed the acting user's id in the query string on several
 * endpoints, so anyone could act as anyone.
 */
public record AuthenticatedUser(Long id, String email, Set<String> roles) {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_COMPANY = "ROLE_COMPANY";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public boolean isAdmin() {
        return roles.contains(ROLE_ADMIN);
    }

    public boolean isCompany() {
        return roles.contains(ROLE_COMPANY);
    }

    /** True when this user is {@code otherUserId}, or is an administrator acting on their behalf. */
    public boolean isSelfOrAdmin(Long otherUserId) {
        return id.equals(otherUserId) || isAdmin();
    }
}
