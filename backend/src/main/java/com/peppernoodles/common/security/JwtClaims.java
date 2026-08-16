package com.peppernoodles.common.security;

/** Names of the custom claims carried by an access token. */
public final class JwtClaims {

    /** The account's login e-mail. */
    public static final String EMAIL = "email";

    /** Granted authorities, e.g. {@code ["ROLE_USER"]}. */
    public static final String ROLES = "roles";

    private JwtClaims() {}
}
