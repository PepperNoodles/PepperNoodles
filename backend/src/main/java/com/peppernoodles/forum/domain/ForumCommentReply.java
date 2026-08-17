package com.peppernoodles.forum.domain;

import com.peppernoodles.user.domain.User;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A reply to a comment. Legacy entity: {@code ForumReplyMessage}.
 *
 * <p>The legacy row carried two user FKs — the replier and the person being
 * replied to — which is preserved as {@code author} and {@code replyToUser} so
 * "A 回覆 B" can still be rendered.
 */
@Entity
@Table(name = "forum_comment_replies")
@Getter
@Setter
@NoArgsConstructor
public class ForumCommentReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false, updatable = false)
    private ForumComment comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_user_id", nullable = false, updatable = false)
    private User author;

    /** Null when replying to the comment itself rather than to another reply. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_user_id")
    private User replyToUser;

    @Column(nullable = false)
    private String body;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    public ForumCommentReply(ForumComment comment, User author, User replyToUser, String body) {
        this.comment = comment;
        this.author = author;
        this.replyToUser = replyToUser;
        this.body = body;
    }

    public boolean isAuthoredBy(Long userId) {
        return author != null && author.getId().equals(userId);
    }
}
