package com.peppernoodles.user.api;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.user.api.dto.WallDtos.*;
import com.peppernoodles.user.service.WallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** 留言牆 and 追蹤, both hung off a user. */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Wall & follows", description = "留言牆、按讚、追蹤")
public class WallController {

    private final WallService wallService;

    public WallController(WallService wallService) {
        this.wallService = wallService;
    }

    @GetMapping("/{userId}/wall")
    @Operation(summary = "使用者留言牆")
    public PageResponse<WallMessageDto> wall(
            @PathVariable Long userId,
            @PageableDefault(size = 20) Pageable pageable,
            @CurrentUser AuthenticatedUser caller) {
        return wallService.getWall(userId, pageable, caller);
    }

    @PostMapping("/{userId}/wall")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "在留言牆留言", description = "Set parentId to reply to an existing message.")
    public void post(
            @PathVariable Long userId,
            @Valid @RequestBody PostWallMessageRequest request,
            @CurrentUser AuthenticatedUser caller) {
        wallService.post(userId, request, caller);
    }

    @DeleteMapping("/wall/{messageId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除留言", description = "Allowed for the author or the wall's owner.")
    public void deleteMessage(@PathVariable Long messageId, @CurrentUser AuthenticatedUser caller) {
        wallService.delete(messageId, caller);
    }

    @PutMapping("/wall/{messageId}/like")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "按讚")
    public void like(@PathVariable Long messageId, @CurrentUser AuthenticatedUser caller) {
        wallService.setLike(messageId, caller.id(), true);
    }

    @DeleteMapping("/wall/{messageId}/like")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "收回讚")
    public void unlike(@PathVariable Long messageId, @CurrentUser AuthenticatedUser caller) {
        wallService.setLike(messageId, caller.id(), false);
    }

    // --- follows -------------------------------------------------------------

    @GetMapping("/{userId}/follow-counts")
    @Operation(summary = "追蹤數與是否已追蹤")
    public FollowCountsDto counts(@PathVariable Long userId, @CurrentUser AuthenticatedUser caller) {
        return wallService.counts(userId, caller);
    }

    @GetMapping("/{userId}/followers")
    @Operation(summary = "粉絲清單")
    public List<FollowUserDto> followers(@PathVariable Long userId) {
        return wallService.followers(userId);
    }

    @GetMapping("/{userId}/following")
    @Operation(summary = "追蹤中清單")
    public List<FollowUserDto> following(@PathVariable Long userId) {
        return wallService.following(userId);
    }

    @PutMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "追蹤")
    public void follow(@PathVariable Long userId, @CurrentUser AuthenticatedUser caller) {
        wallService.follow(caller.id(), userId);
    }

    @DeleteMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "取消追蹤")
    public void unfollow(@PathVariable Long userId, @CurrentUser AuthenticatedUser caller) {
        wallService.unfollow(caller.id(), userId);
    }
}
