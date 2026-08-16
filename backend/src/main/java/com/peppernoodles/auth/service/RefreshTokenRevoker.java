package com.peppernoodles.auth.service;

import com.peppernoodles.auth.repository.RefreshTokenRepository;
import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Revokes every session for an account in its own transaction.
 *
 * <p>This exists because of a subtle interaction: replay detection needs to
 * revoke the whole token family and <em>then</em> reject the request by
 * throwing. If both happened in the caller's transaction, the throw would roll
 * the revocation back and the leaked family would stay usable — which is
 * exactly the case the detection is meant to shut down.
 *
 * <p>{@code REQUIRES_NEW} commits the revocation independently, so it survives
 * the caller's rollback.
 */
@Component
public class RefreshTokenRevoker {

    private final RefreshTokenRepository refreshTokens;

    public RefreshTokenRevoker(RefreshTokenRepository refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int revokeAllForUser(Long userId) {
        return refreshTokens.revokeAllForUser(userId, Instant.now());
    }
}
