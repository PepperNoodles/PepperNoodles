package com.peppernoodles.forum.domain;

import com.peppernoodles.tag.domain.FoodTag;
import com.peppernoodles.user.domain.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 專欄文章 / 論壇貼文. Legacy entity: {@code Forum}.
 *
 * <p>There is no title column — the legacy forum stored only {@code content},
 * and post lists show a truncated body, so one was not invented here.
 *
 * <p>Bookmarks (legacy {@code forumCollections}) are the owning side of the
 * many-to-many; the like/bookmark count is derived from the join table rather
 * than denormalised onto the row as {@code likeAmount} was.
 */
@Entity
@Table(name = "forum_posts")
@Getter
@Setter
@NoArgsConstructor
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_user_id", nullable = false, updatable = false)
    private User author;

    @Column(nullable = false)
    private String body;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "forum_post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<FoodTag> tags = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "forum_bookmarks",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> bookmarkedBy = new LinkedHashSet<>();

    public ForumPost(User author, String body) {
        this.author = author;
        this.body = body;
    }

    public boolean isAuthoredBy(Long userId) {
        return author != null && author.getId().equals(userId);
    }
}
