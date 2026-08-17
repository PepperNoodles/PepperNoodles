package com.peppernoodles.forum.api;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.forum.api.dto.ForumDtos.*;
import com.peppernoodles.forum.service.ForumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/forum")
@Tag(name = "Forum", description = "論壇 / 專欄文章")
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping("/posts")
    @Operation(summary = "文章列表", description = "Optionally filtered by tag or author.")
    public PageResponse<PostSummary> list(
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(required = false) Long authorId,
            @PageableDefault(size = 10) Pageable pageable,
            @CurrentUser AuthenticatedUser caller) {
        return forumService.list(tagIds, authorId, pageable, caller);
    }

    @GetMapping("/posts/{id}")
    @Operation(summary = "文章內容與留言")
    public PostDetail get(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        return forumService.get(id, caller);
    }

    @GetMapping("/bookmarks")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "我收藏的文章")
    public PageResponse<PostSummary> bookmarks(
            @CurrentUser AuthenticatedUser caller, @PageableDefault(size = 10) Pageable pageable) {
        return forumService.listBookmarked(caller.id(), pageable, caller);
    }

    @PostMapping("/posts")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "發表文章")
    public ResponseEntity<Void> create(
            @Valid @RequestBody SavePostRequest request, @CurrentUser AuthenticatedUser caller) {
        Long id = forumService.create(request, caller);
        return ResponseEntity.created(URI.create("/api/v1/forum/posts/" + id)).build();
    }

    @PutMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "修改文章")
    public void update(
            @PathVariable Long id,
            @Valid @RequestBody SavePostRequest request,
            @CurrentUser AuthenticatedUser caller) {
        forumService.update(id, request, caller);
    }

    @DeleteMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除文章")
    public void delete(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        forumService.delete(id, caller);
    }

    @PostMapping(path = "/posts/{id}/image", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "上傳文章圖片")
    public ImageUploaded uploadImage(
            @PathVariable Long id, @RequestPart("file") MultipartFile file, @CurrentUser AuthenticatedUser caller) {
        return new ImageUploaded(forumService.uploadImage(id, file, caller));
    }

    @PutMapping("/posts/{id}/bookmark")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "收藏文章")
    public void bookmark(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        forumService.setBookmark(id, caller.id(), true);
    }

    @DeleteMapping("/posts/{id}/bookmark")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "取消收藏")
    public void unbookmark(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        forumService.setBookmark(id, caller.id(), false);
    }

    // --- comments ------------------------------------------------------------

    @PostMapping("/posts/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "留言")
    public void addComment(
            @PathVariable Long id,
            @Valid @RequestBody SaveCommentRequest request,
            @CurrentUser AuthenticatedUser caller) {
        forumService.addComment(id, request, caller);
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除留言", description = "Allowed for the commenter or the post's author.")
    public void deleteComment(@PathVariable Long commentId, @CurrentUser AuthenticatedUser caller) {
        forumService.deleteComment(commentId, caller);
    }

    @PostMapping("/comments/{commentId}/replies")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "回覆留言")
    public void addReply(
            @PathVariable Long commentId,
            @Valid @RequestBody SaveReplyRequest request,
            @CurrentUser AuthenticatedUser caller) {
        forumService.addReply(commentId, request, caller);
    }

    @DeleteMapping("/replies/{replyId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除回覆")
    public void deleteReply(@PathVariable Long replyId, @CurrentUser AuthenticatedUser caller) {
        forumService.deleteReply(replyId, caller);
    }

    public record ImageUploaded(String imageUrl) {}
}
