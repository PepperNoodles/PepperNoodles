package com.peppernoodles.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 好友關係. Legacy entity: {@code FriendList}.
 *
 * <p>The legacy table stored the relationship state in a free-text
 * {@code friendship} column and let (a,b) and (b,a) exist as independent rows,
 * so a pair could be simultaneously accepted and pending. A unique constraint on
 * the ordered pair now makes duplicates impossible.
 */
@Entity
@Table(name = "friendships")
@Getter
@Setter
@NoArgsConstructor
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_user_id", nullable = false, updatable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "addressee_user_id", nullable = false, updatable = false)
    private User addressee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "responded_at")
    private Instant respondedAt;

    public Friendship(User requester, User addressee) {
        this.requester = requester;
        this.addressee = addressee;
    }

    public void accept() {
        this.status = Status.ACCEPTED;
        this.respondedAt = Instant.now();
    }

    public void decline() {
        this.status = Status.DECLINED;
        this.respondedAt = Instant.now();
    }

    public void block() {
        this.status = Status.BLOCKED;
        this.respondedAt = Instant.now();
    }

    /** The other party, given one side of the relationship. */
    public User counterpartOf(Long userId) {
        return requester.getId().equals(userId) ? addressee : requester;
    }

    public boolean involves(Long userId) {
        return requester.getId().equals(userId) || addressee.getId().equals(userId);
    }

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED,
        BLOCKED
    }
}
