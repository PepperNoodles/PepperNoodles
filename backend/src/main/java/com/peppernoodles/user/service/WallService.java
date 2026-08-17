package com.peppernoodles.user.service;

import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.user.api.dto.WallDtos.*;
import com.peppernoodles.user.domain.Follow;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.domain.UserProfile;
import com.peppernoodles.user.domain.WallMessage;
import com.peppernoodles.user.repository.FollowRepository;
import com.peppernoodles.user.repository.UserRepository;
import com.peppernoodles.user.repository.WallMessageRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 留言牆 and 追蹤. */
@Service
public class WallService {

    private final WallMessageRepository messages;
    private final FollowRepository follows;
    private final UserRepository users;
    private final StorageService storage;

    public WallService(
            WallMessageRepository messages,
            FollowRepository follows,
            UserRepository users,
            StorageService storage) {
        this.messages = messages;
        this.follows = follows;
        this.users = users;
        this.storage = storage;
    }

    // --- wall ----------------------------------------------------------------

    /**
     * A page of top-level messages with their replies.
     *
     * <p>Replies and like counts are fetched in one batch each, keyed by id, so
     * a page of twenty messages costs four queries rather than sixty.
     */
    @Transactional(readOnly = true)
    public PageResponse<WallMessageDto> getWall(Long ownerId, Pageable pageable, AuthenticatedUser caller) {
        var page = messages.findByWallOwnerIdAndParentIsNullOrderByCreatedAtDesc(ownerId, pageable);
        List<Long> topLevelIds = page.getContent().stream().map(WallMessage::getId).toList();

        List<WallMessage> replies =
                topLevelIds.isEmpty() ? List.of() : messages.findByParentIdInOrderByCreatedAtAsc(topLevelIds);

        List<Long> allIds = new java.util.ArrayList<>(topLevelIds);
        replies.forEach(r -> allIds.add(r.getId()));

        Map<Long, Long> likeCounts = allIds.isEmpty()
                ? Map.of()
                : messages.countLikes(allIds).stream()
                        .collect(Collectors.toMap(
                                WallMessageRepository.LikeCountRow::getMessageId,
                                WallMessageRepository.LikeCountRow::getLikeCount));

        Set<Long> likedByMe = (caller == null || allIds.isEmpty())
                ? Set.of()
                : new HashSet<>(messages.findLikedIdsBy(allIds, caller.id()));

        Map<Long, List<WallMessage>> repliesByParent = new HashMap<>();
        replies.forEach(r -> repliesByParent
                .computeIfAbsent(r.getParent().getId(), k -> new java.util.ArrayList<>())
                .add(r));

        return PageResponse.of(page, message -> toDto(message, repliesByParent, likeCounts, likedByMe, caller));
    }

    @Transactional
    public Long post(Long ownerId, PostWallMessageRequest request, AuthenticatedUser caller) {
        User owner = users.findById(ownerId).orElseThrow(() -> NotFoundException.of("使用者", ownerId));
        User author = users.findById(caller.id()).orElseThrow(() -> NotFoundException.of("使用者", caller.id()));

        WallMessage parent = null;
        if (request.parentId() != null) {
            parent = messages.findById(request.parentId())
                    .orElseThrow(() -> NotFoundException.of("留言", request.parentId()));
            // A reply must belong to the same wall, and nesting stops at one level.
            if (!parent.getWallOwner().getId().equals(ownerId)) {
                throw new ValidationException("這則留言不在這個留言牆上。");
            }
            if (parent.getParent() != null) {
                throw new ValidationException("回覆不能再被回覆。");
            }
        }

        return messages.save(new WallMessage(owner, author, request.body(), parent)).getId();
    }

    @Transactional
    public void delete(Long messageId, AuthenticatedUser caller) {
        WallMessage message =
                messages.findDetailedById(messageId).orElseThrow(() -> NotFoundException.of("留言", messageId));
        if (!message.isDeletableBy(caller.id()) && !caller.isAdmin()) {
            throw new ForbiddenException("只有留言者或版面主人可以刪除。");
        }
        messages.delete(message);
    }

    /** 按讚 — idempotent in both directions. */
    @Transactional
    public void setLike(Long messageId, Long userId, boolean liked) {
        WallMessage message =
                messages.findById(messageId).orElseThrow(() -> NotFoundException.of("留言", messageId));
        User user = users.findById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));
        if (liked) {
            message.getLikedBy().add(user);
        } else {
            message.getLikedBy().remove(user);
        }
    }

    // --- follows -------------------------------------------------------------

    @Transactional
    public void follow(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new ValidationException("不能追蹤自己。");
        }
        if (!users.existsById(followeeId)) {
            throw NotFoundException.of("使用者", followeeId);
        }
        if (follows.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new ConflictException("已經追蹤過這位使用者了。");
        }
        follows.save(new Follow(followerId, followeeId));
    }

    @Transactional
    public void unfollow(Long followerId, Long followeeId) {
        follows.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Transactional(readOnly = true)
    public FollowCountsDto counts(Long userId, AuthenticatedUser caller) {
        return new FollowCountsDto(
                follows.countByFolloweeId(userId),
                follows.countByFollowerId(userId),
                caller != null && follows.existsByFollowerIdAndFolloweeId(caller.id(), userId));
    }

    @Transactional(readOnly = true)
    public List<FollowUserDto> followers(Long userId) {
        return follows.findByFolloweeId(userId).stream()
                .map(f -> toFollowDto(f.getFollower(), f.getCreatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowUserDto> following(Long userId) {
        return follows.findByFollowerId(userId).stream()
                .map(f -> toFollowDto(f.getFollowee(), f.getCreatedAt()))
                .toList();
    }

    // --- mapping -------------------------------------------------------------

    private WallMessageDto toDto(
            WallMessage message,
            Map<Long, List<WallMessage>> repliesByParent,
            Map<Long, Long> likeCounts,
            Set<Long> likedByMe,
            AuthenticatedUser caller) {

        List<WallMessageDto> replies = repliesByParent.getOrDefault(message.getId(), List.of()).stream()
                .map(reply -> toDto(reply, Map.of(), likeCounts, likedByMe, caller))
                .toList();

        return new WallMessageDto(
                message.getId(),
                toWallAuthor(message.getAuthor()),
                message.getBody(),
                likeCounts.getOrDefault(message.getId(), 0L),
                likedByMe.contains(message.getId()),
                caller != null && (message.isDeletableBy(caller.id()) || caller.isAdmin()),
                message.getCreatedAt(),
                replies);
    }

    private WallAuthorDto toWallAuthor(User user) {
        UserProfile profile = user.getProfile();
        return new WallAuthorDto(
                user.getId(),
                profile == null || profile.getNickname() == null ? "會員" : profile.getNickname(),
                profile == null ? null : storage.publicUrl(StorageBucket.USER_AVATARS, profile.getAvatarPath()));
    }

    private FollowUserDto toFollowDto(User user, java.time.Instant since) {
        UserProfile profile = user.getProfile();
        return new FollowUserDto(
                user.getId(),
                profile == null || profile.getNickname() == null ? "會員" : profile.getNickname(),
                profile == null ? null : storage.publicUrl(StorageBucket.USER_AVATARS, profile.getAvatarPath()),
                since);
    }
}
