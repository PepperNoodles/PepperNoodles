package com.peppernoodles.auth.domain;

import com.peppernoodles.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Single-use token backing 忘記密碼. */
@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken extends AbstractToken {

    @Column(name = "consumed_at")
    private Instant consumedAt;

    public PasswordResetToken(User user, String tokenHash, Instant expiresAt) {
        super(user, tokenHash, expiresAt);
    }

    public boolean isConsumed() {
        return consumedAt != null;
    }

    @Override
    public boolean isUsable() {
        return !isConsumed() && !isExpired();
    }

    public void consume() {
        this.consumedAt = Instant.now();
    }
}
