package com.peppernoodles.forum.domain;

import com.peppernoodles.user.domain.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A comment on a post. Legacy entity: {@code ForumMessageBox}. */
@Entity
@Table(name = "forum_comments")
@Getter
@Setter
@NoArgsConstructor
public class ForumComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    private ForumPost post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_user_id", nullable = false, updatable = false)
    private User author;

    @Column(nullable = false)
    private String body;

    /** Optional 1–5 rating, carried over from the legacy column. */
    @Column
    private Short score;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt asc")
    private List<ForumCommentReply> replies = new ArrayList<>();

    public ForumComment(ForumPost post, User author, String body, Short score) {
        this.post = post;
        this.author = author;
        this.body = body;
        this.score = score;
    }

    public boolean isAuthoredBy(Long userId) {
        return author != null && author.getId().equals(userId);
    }
}
