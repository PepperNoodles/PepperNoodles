package com.peppernoodles.chat.service;

import com.peppernoodles.chat.api.dto.ChatDtos.ChatMessageDto;
import com.peppernoodles.chat.api.dto.ChatDtos.ConversationDto;
import com.peppernoodles.chat.domain.ChatMessage;
import com.peppernoodles.chat.repository.ChatMessageRepository;
import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.user.domain.Friendship.Status;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.repository.FriendshipRepository;
import com.peppernoodles.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 好友聊天. */
@Service
public class ChatService {

    private final ChatMessageRepository messages;
    private final FriendshipRepository friendships;
    private final UserRepository users;
    private final StorageService storage;

    public ChatService(
            ChatMessageRepository messages,
            FriendshipRepository friendships,
            UserRepository users,
            StorageService storage) {
        this.messages = messages;
        this.friendships = friendships;
        this.users = users;
        this.storage = storage;
    }

    /**
     * Persists a message and returns it.
     *
     * <p>Only accepted friends may message each other, which also makes the
     * endpoint useless for spam.
     */
    @Transactional
    public ChatMessageDto send(Long senderId, Long recipientId, String body) {
        if (senderId.equals(recipientId)) {
            throw new ValidationException("不能傳訊息給自己。");
        }
        requireFriends(senderId, recipientId);

        User sender = users.findById(senderId).orElseThrow(() -> NotFoundException.of("使用者", senderId));
        User recipient =
                users.findById(recipientId).orElseThrow(() -> NotFoundException.of("使用者", recipientId));

        ChatMessage saved = messages.save(new ChatMessage(sender, recipient, body));
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<ChatMessageDto> conversation(Long callerId, Long otherId, Pageable pageable) {
        requireFriends(callerId, otherId);
        return PageResponse.of(messages.findConversation(callerId, otherId, pageable), ChatService::toDto);
    }

    @Transactional
    public void markRead(Long callerId, Long otherId) {
        messages.markConversationRead(callerId, otherId, Instant.now());
    }

    @Transactional(readOnly = true)
    public long unreadCount(Long userId) {
        return messages.countByRecipientIdAndReadAtIsNull(userId);
    }

    @Transactional(readOnly = true)
    public List<ConversationDto> conversations(Long userId) {
        var rows = messages.findConversations(userId);
        if (rows.isEmpty()) {
            return List.of();
        }

        Map<Long, User> partners = users.findAllById(
                        rows.stream().map(r -> r.getPartnerId()).toList())
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return rows.stream()
                .map(row -> {
                    User partner = partners.get(row.getPartnerId());
                    var profile = partner == null ? null : partner.getProfile();
                    return new ConversationDto(
                            row.getPartnerId(),
                            profile == null ? "使用者" : profile.getNickname(),
                            profile == null
                                    ? null
                                    : storage.publicUrl(StorageBucket.USER_AVATARS, profile.getAvatarPath()),
                            row.getLastMessage(),
                            row.getLastMessageAt(),
                            row.getUnreadCount() == null ? 0 : row.getUnreadCount());
                })
                .sorted((a, b) -> b.lastMessageAt().compareTo(a.lastMessageAt()))
                .toList();
    }

    private void requireFriends(Long a, Long b) {
        boolean friends = friendships
                .findBetween(a, b)
                .map(f -> f.getStatus() == Status.ACCEPTED)
                .orElse(false);
        if (!friends) {
            throw new ForbiddenException("只能與好友聊天。");
        }
    }

    private static ChatMessageDto toDto(ChatMessage m) {
        return new ChatMessageDto(
                m.getId(),
                m.getSender().getId(),
                m.getRecipient().getId(),
                m.getBody(),
                m.getCreatedAt(),
                m.getReadAt());
    }
}
