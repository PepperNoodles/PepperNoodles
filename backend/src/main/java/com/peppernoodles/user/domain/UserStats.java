package com.peppernoodles.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Per-account activity counters driving the membership tier. Legacy entity: {@code LevelDetail}. */
@Entity
@Table(name = "user_stats")
@Getter
@Setter
@NoArgsConstructor
public class UserStats {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String tier = "BRONZE";

    @Column(name = "post_count", nullable = false)
    private int postCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Column(name = "follower_count", nullable = false)
    private int followerCount;

    @Column(name = "reply_count", nullable = false)
    private int replyCount;

    @Column(name = "login_count", nullable = false)
    private int loginCount;

    @Column(name = "purchase_count", nullable = false)
    private int purchaseCount;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    public UserStats(User user) {
        this.user = user;
    }

    public void recordLogin() {
        loginCount++;
    }
}
