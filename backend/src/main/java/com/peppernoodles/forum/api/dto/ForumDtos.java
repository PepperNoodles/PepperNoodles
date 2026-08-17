package com.peppernoodles.forum.api.dto;

import com.peppernoodles.restaurant.api.dto.TagSummary;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

public final class ForumDtos {

    private ForumDtos() {}

    public record AuthorDto(Long userId, String displayName, String avatarUrl) {}

    public record PostSummary(
            Long id, AuthorDto author, String excerpt, String imageUrl, List<TagSummary> tags,
            long commentCount, long bookmarkCount, boolean bookmarked, boolean editable,
            Instant createdAt) {}

    public record PostDetail(
            Long id, AuthorDto author, String body, String imageUrl, List<TagSummary> tags,
            long bookmarkCount, boolean bookmarked, boolean editable,
            List<CommentDto> comments, Instant createdAt, Instant updatedAt) {}

    public record CommentDto(
            Long id, AuthorDto author, String body, Short score, boolean editable,
            Instant createdAt, List<ReplyDto> replies) {}

    public record ReplyDto(
            Long id, AuthorDto author, AuthorDto replyTo, String body, boolean editable, Instant createdAt) {}

    public record SavePostRequest(
            @NotBlank @Size(max = 5000, message = "內容長度不可超過 5000 字") String body,
            List<Long> tagIds) {}

    public record SaveCommentRequest(
            @NotBlank @Size(max = 2000, message = "留言長度不可超過 2000 字") String body,
            @Min(1) @Max(5) Short score) {}

    public record SaveReplyRequest(
            @NotBlank @Size(max = 2000, message = "回覆長度不可超過 2000 字") String body,
            Long replyToUserId) {}
}
