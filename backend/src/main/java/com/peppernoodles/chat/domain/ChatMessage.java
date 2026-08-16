package com.peppernoodles.chat.domain;

import com.peppernoodles.user.domain.User;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A direct message between two members. Legacy entity: {@code SocketMessage}. */
@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_user_id", nullable = false, updatable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_user_id", nullable = false, updatable = false)
    private User recipient;

    @Column(nullable = false)
    private String body;

    @Column(name = "read_at")
    private Instant readAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public ChatMessage(User sender, User recipient, String body) {
        this.sender = sender;
        this.recipient = recipient;
        this.body = body;
    }

    public void markRead() {
        if (readAt == null) {
            readAt = Instant.now();
        }
    }
}
