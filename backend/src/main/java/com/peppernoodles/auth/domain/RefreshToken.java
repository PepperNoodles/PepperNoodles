package com.peppernoodles.auth.domain;

import com.peppernoodles.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A rotating refresh token.
 *
 * <p>Every successful refresh revokes the presented token and issues a new one.
 * Presenting an already-revoked token is treated as replay and revokes the whole
 * family for that account.
 */
@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken extends AbstractToken {

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "user_agent")
    private String userAgent;

    public RefreshToken(User user, String tokenHash, Instant expiresAt, String userAgent) {
        super(user, tokenHash, expiresAt);
        this.userAgent = userAgent;
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    @Override
    public boolean isUsable() {
        return !isRevoked() && !isExpired();
    }

    public void revoke() {
        if (revokedAt == null) {
            revokedAt = Instant.now();
        }
    }
}
