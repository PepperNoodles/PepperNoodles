package com.peppernoodles.user.api;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.user.api.dto.FriendDto;
import com.peppernoodles.user.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/friends")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Friends", description = "好友系統")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    @Operation(summary = "好友清單")
    public List<FriendDto> friends(@CurrentUser AuthenticatedUser caller) {
        return friendService.listFriends(caller.id());
    }

    @GetMapping("/requests/incoming")
    @Operation(summary = "收到的好友邀請")
    public List<FriendDto> incoming(@CurrentUser AuthenticatedUser caller) {
        return friendService.listIncomingRequests(caller.id());
    }

    @GetMapping("/requests/outgoing")
    @Operation(summary = "送出的好友邀請")
    public List<FriendDto> outgoing(@CurrentUser AuthenticatedUser caller) {
        return friendService.listOutgoingRequests(caller.id());
    }

    @PostMapping("/requests/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "送出好友邀請")
    public void request(@PathVariable Long userId, @CurrentUser AuthenticatedUser caller) {
        friendService.sendRequest(caller.id(), userId);
    }

    @PostMapping("/requests/{friendshipId}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "接受好友邀請", description = "Only the addressee may accept.")
    public void accept(@PathVariable Long friendshipId, @CurrentUser AuthenticatedUser caller) {
        friendService.respond(friendshipId, caller.id(), true);
    }

    @PostMapping("/requests/{friendshipId}/decline")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "拒絕好友邀請")
    public void decline(@PathVariable Long friendshipId, @CurrentUser AuthenticatedUser caller) {
        friendService.respond(friendshipId, caller.id(), false);
    }

    @DeleteMapping("/{friendshipId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "解除好友關係")
    public void remove(@PathVariable Long friendshipId, @CurrentUser AuthenticatedUser caller) {
        friendService.remove(friendshipId, caller.id());
    }
}
