package com.peppernoodles.user.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 留言牆 message. Legacy entity: {@code MessageBox}.
 *
 * <p>Two distinct people are involved: {@code wallOwner} is whose wall it is,
 * {@code author} is who wrote it. Replies are the same entity pointing at a
 * {@code parent}, matching the legacy self-referencing design.
 *
 * <p>Likes come from the join table rather than the legacy denormalised
 * {@code likeAmount} column, which several controllers updated by hand.
 */
@Entity
@Table(name = "wall_messages")
@Getter
@Setter
@NoArgsConstructor
public class WallMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wall_owner_user_id", nullable = false, updatable = false)
    private User wallOwner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_user_id", nullable = false, updatable = false)
    private User author;

    /** Null for a top-level message; set when this is a reply. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private WallMessage parent;

    @Column(nullable = false)
    private String body;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt asc")
    private List<WallMessage> replies = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "wall_message_likes",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedBy = new LinkedHashSet<>();

    public WallMessage(User wallOwner, User author, String body, WallMessage parent) {
        this.wallOwner = wallOwner;
        this.author = author;
        this.body = body;
        this.parent = parent;
    }

    public boolean isAuthoredBy(Long userId) {
        return author != null && author.getId().equals(userId);
    }

    /** The wall's owner may remove anything posted on their own wall. */
    public boolean isDeletableBy(Long userId) {
        return isAuthoredBy(userId) || (wallOwner != null && wallOwner.getId().equals(userId));
    }
}
