package com.peppernoodles.user.service;

import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.user.api.dto.FriendDto;
import com.peppernoodles.user.domain.Friendship;
import com.peppernoodles.user.domain.Friendship.Status;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.domain.UserProfile;
import com.peppernoodles.user.repository.FriendshipRepository;
import com.peppernoodles.user.repository.UserRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 好友系統 — requests, acceptance, and the friend list. */
@Service
public class FriendService {

    private static final Logger log = LoggerFactory.getLogger(FriendService.class);

    private final FriendshipRepository friendships;
    private final UserRepository users;
    private final StorageService storage;

    public FriendService(FriendshipRepository friendships, UserRepository users, StorageService storage) {
        this.friendships = friendships;
        this.users = users;
        this.storage = storage;
    }

    @Transactional(readOnly = true)
    public List<FriendDto> listFriends(Long userId) {
        return friendships.findAllForUser(userId, Status.ACCEPTED).stream()
                .map(f -> toDto(f, userId))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendDto> listIncomingRequests(Long userId) {
        return friendships.findByAddresseeIdAndStatus(userId, Status.PENDING).stream()
                .map(f -> toDto(f, userId))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendDto> listOutgoingRequests(Long userId) {
        return friendships.findByRequesterIdAndStatus(userId, Status.PENDING).stream()
                .map(f -> toDto(f, userId))
                .toList();
    }

    @Transactional
    public void sendRequest(Long requesterId, Long addresseeId) {
        if (requesterId.equals(addresseeId)) {
            throw new ValidationException("不能加自己為好友。");
        }

        User addressee =
                users.findById(addresseeId).orElseThrow(() -> NotFoundException.of("使用者", addresseeId));

        friendships.findBetween(requesterId, addresseeId).ifPresent(existing -> {
            throw switch (existing.getStatus()) {
                case ACCEPTED -> new ConflictException("你們已經是好友了。");
                case PENDING -> new ConflictException("已經有一筆待處理的好友邀請。");
                case BLOCKED -> new ForbiddenException("無法傳送好友邀請。");
                case DECLINED -> new ConflictException("先前的邀請已被拒絕。");
            };
        });

        User requester = users.findById(requesterId).orElseThrow(() -> NotFoundException.of("使用者", requesterId));
        friendships.save(new Friendship(requester, addressee));
        log.info("User {} sent a friend request to {}", requesterId, addresseeId);
    }

    @Transactional
    public void respond(Long friendshipId, Long callerId, boolean accept) {
        Friendship friendship =
                friendships.findById(friendshipId).orElseThrow(() -> NotFoundException.of("好友邀請", friendshipId));

        // Only the person who received the request may answer it.
        if (!friendship.getAddressee().getId().equals(callerId)) {
            throw new ForbiddenException("只有被邀請的人可以回應這筆邀請。");
        }
        if (friendship.getStatus() != Status.PENDING) {
            throw new ConflictException("這筆邀請已經處理過了。");
        }

        if (accept) {
            friendship.accept();
        } else {
            friendship.decline();
        }
    }

    @Transactional
    public void remove(Long friendshipId, Long callerId) {
        Friendship friendship =
                friendships.findById(friendshipId).orElseThrow(() -> NotFoundException.of("好友關係", friendshipId));
        if (!friendship.involves(callerId)) {
            throw new ForbiddenException("您不是這段好友關係的一方。");
        }
        friendships.delete(friendship);
    }

    private FriendDto toDto(Friendship friendship, Long callerId) {
        User other = friendship.counterpartOf(callerId);
        UserProfile profile = other.getProfile();

        return new FriendDto(
                friendship.getId(),
                other.getId(),
                profile == null ? "使用者" : profile.getNickname(),
                profile == null ? null : storage.publicUrl(StorageBucket.USER_AVATARS, profile.getAvatarPath()),
                friendship.getStatus().name(),
                friendship.getAddressee().getId().equals(callerId),
                friendship.getRespondedAt() != null ? friendship.getRespondedAt() : friendship.getCreatedAt());
    }
}
