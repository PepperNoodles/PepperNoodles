package com.peppernoodles.chat.api;

import com.peppernoodles.chat.api.dto.ChatDtos.ChatMessageDto;
import com.peppernoodles.chat.api.dto.ChatDtos.ConversationDto;
import com.peppernoodles.chat.api.dto.ChatDtos.SendMessageRequest;
import com.peppernoodles.chat.config.StompAuthChannelInterceptor.StompPrincipal;
import com.peppernoodles.chat.service.ChatService;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.common.web.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Chat over both transports: STOMP for live delivery, REST for history.
 *
 * <p>The sender is always the authenticated principal — never a field in the
 * payload, which is how the legacy implementation identified the sender.
 */
@RestController
@RequestMapping("/api/v1/chat")
@Tag(name = "Chat", description = "好友聊天")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    /** STOMP: client sends to /app/chat.send. */
    @MessageMapping("/chat.send")
    public void handleStompMessage(@Valid @Payload SendMessageRequest request, Principal principal) {
        AuthenticatedUser sender = ((StompPrincipal) principal).user();
        ChatMessageDto saved = chatService.send(sender.id(), request.recipientId(), request.body());

        // Deliver to the recipient's private queue, and echo to the sender so
        // their other open tabs stay in sync.
        messagingTemplate.convertAndSendToUser(String.valueOf(request.recipientId()), "/queue/messages", saved);
        messagingTemplate.convertAndSendToUser(String.valueOf(sender.id()), "/queue/messages", saved);
    }

    @GetMapping("/conversations")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "對話清單")
    public List<ConversationDto> conversations(@CurrentUser AuthenticatedUser caller) {
        return chatService.conversations(caller.id());
    }

    @GetMapping("/conversations/{otherUserId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "對話紀錄", description = "Newest first. Friends only.")
    public PageResponse<ChatMessageDto> history(
            @PathVariable Long otherUserId,
            @CurrentUser AuthenticatedUser caller,
            @PageableDefault(size = 30) Pageable pageable) {
        return chatService.conversation(caller.id(), otherUserId, pageable);
    }

    @PostMapping("/conversations/{otherUserId}/read")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "標記對話為已讀")
    public void markRead(@PathVariable Long otherUserId, @CurrentUser AuthenticatedUser caller) {
        chatService.markRead(caller.id(), otherUserId);
    }

    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "未讀訊息數")
    public UnreadCount unreadCount(@CurrentUser AuthenticatedUser caller) {
        return new UnreadCount(chatService.unreadCount(caller.id()));
    }

    /** REST fallback for clients without a live socket. */
    @PostMapping("/messages")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "傳送訊息 (REST)")
    public ChatMessageDto send(
            @Valid @RequestBody SendMessageRequest request, @CurrentUser AuthenticatedUser caller) {
        ChatMessageDto saved = chatService.send(caller.id(), request.recipientId(), request.body());
        messagingTemplate.convertAndSendToUser(String.valueOf(request.recipientId()), "/queue/messages", saved);
        return saved;
    }

    public record UnreadCount(long count) {}
}
