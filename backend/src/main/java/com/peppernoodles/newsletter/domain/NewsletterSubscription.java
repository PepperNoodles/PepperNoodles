package com.peppernoodles.newsletter.domain;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 電子報訂閱.
 *
 * <p>Double opt-in: {@link #isMailable()} is only true once the reader has
 * followed the confirmation link and has not since opted out. Unconfirmed rows
 * exist so a repeat sign-up can re-send the confirmation without creating
 * duplicates, but they are never mailed anything else.
 */
@Entity
@Table(name = "newsletter_subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class NewsletterSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name = "unsubscribed_at")
    private Instant unsubscribedAt;

    @Column(name = "confirm_token_hash")
    private String confirmTokenHash;

    @Column(name = "confirm_expires_at")
    private Instant confirmExpiresAt;

    /** Permanent — it has to keep working in mail sent months ago. */
    @Column(name = "unsubscribe_token_hash", nullable = false, unique = true)
    private String unsubscribeTokenHash;

    /** Where the sign-up came from, e.g. "home". */
    @Column
    private String source;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    public NewsletterSubscription(String email, String unsubscribeTokenHash, String source) {
        this.email = email;
        this.unsubscribeTokenHash = unsubscribeTokenHash;
        this.source = source;
    }

    public boolean isConfirmed() {
        return confirmedAt != null;
    }

    /** Only these rows may receive a newsletter. */
    public boolean isMailable() {
        return confirmedAt != null && unsubscribedAt == null;
    }

    public void issueConfirmToken(String tokenHash, Instant expiresAt) {
        this.confirmTokenHash = tokenHash;
        this.confirmExpiresAt = expiresAt;
    }

    public void confirm() {
        this.confirmedAt = Instant.now();
        this.unsubscribedAt = null;
        // Single use: the link stops working once it has been followed.
        this.confirmTokenHash = null;
        this.confirmExpiresAt = null;
    }

    public void unsubscribe() {
        this.unsubscribedAt = Instant.now();
    }

    public boolean confirmTokenExpired(Instant now) {
        return confirmExpiresAt == null || confirmExpiresAt.isBefore(now);
    }
}
