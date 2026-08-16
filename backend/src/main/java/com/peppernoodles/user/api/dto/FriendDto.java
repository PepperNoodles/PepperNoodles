package com.peppernoodles.user.api.dto;

import java.time.Instant;

/** One entry in the friend list or a pending request. */
public record FriendDto(
        Long friendshipId,
        Long userId,
        String nickname,
        String avatarUrl,
        String status,
        /** True when the other party sent the request and the caller must answer. */
        boolean incoming,
        Instant since) {}
