package com.peppernoodles.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

/** 留言牆 and 追蹤. */
public final class WallDtos {

    private WallDtos() {}

    public record WallAuthorDto(Long userId, String displayName, String avatarUrl) {}

    public record WallMessageDto(
            Long id, WallAuthorDto author, String body, long likeCount, boolean likedByMe,
            boolean deletable, Instant createdAt, List<WallMessageDto> replies) {}

    public record PostWallMessageRequest(
            @NotBlank @Size(max = 2000, message = "留言長度不可超過 2000 字") String body,
            /** Set to reply to an existing message on the same wall. */
            Long parentId) {}

    public record FollowUserDto(
            Long userId, String displayName, String avatarUrl, Instant since) {}

    public record FollowCountsDto(long followers, long following, boolean followedByMe) {}
}
