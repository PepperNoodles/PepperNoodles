package com.peppernoodles.user.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 追蹤 — a one-way follow. Legacy table: {@code UserFollowerForm}.
 *
 * <p>Unlike {@link Friendship} this needs no acceptance; the composite key makes
 * following twice impossible.
 */
@Entity
@Table(name = "user_follows")
@IdClass(Follow.Key.class)
@Getter
@Setter
@NoArgsConstructor
public class Follow {

    @Id
    @Column(name = "follower_user_id")
    private Long followerId;

    @Id
    @Column(name = "followee_user_id")
    private Long followeeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_user_id", insertable = false, updatable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "followee_user_id", insertable = false, updatable = false)
    private User followee;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public Follow(Long followerId, Long followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    /** Composite primary key (follower_user_id, followee_user_id). */
    public static class Key implements Serializable {
        private Long followerId;
        private Long followeeId;

        public Key() {}

        public Key(Long followerId, Long followeeId) {
            this.followerId = followerId;
            this.followeeId = followeeId;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Key k
                    && Objects.equals(followerId, k.followerId)
                    && Objects.equals(followeeId, k.followeeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(followerId, followeeId);
        }
    }
}
