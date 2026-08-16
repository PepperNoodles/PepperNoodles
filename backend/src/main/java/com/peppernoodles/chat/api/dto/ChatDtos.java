package com.peppernoodles.chat.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public final class ChatDtos {

    private ChatDtos() {}

    /** Payload of a STOMP SEND to /app/chat.send. The sender is taken from the session. */
    public record SendMessageRequest(
            @NotNull Long recipientId,
            @NotBlank @Size(max = 2000, message = "訊息長度不可超過 2000 字") String body) {}

    public record ChatMessageDto(
            Long id, Long senderId, Long recipientId, String body, Instant createdAt, Instant readAt) {}

    /** One row of the conversation list. */
    public record ConversationDto(
            Long partnerId, String partnerName, String partnerAvatarUrl,
            String lastMessage, Instant lastMessageAt, long unreadCount) {}
}
