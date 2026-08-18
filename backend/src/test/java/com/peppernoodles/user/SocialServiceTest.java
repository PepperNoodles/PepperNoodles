package com.peppernoodles.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.api.dto.WallDtos.PostWallMessageRequest;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.service.FriendService;
import com.peppernoodles.user.service.WallService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/** 好友、留言牆、追蹤. */
class SocialServiceTest extends IntegrationTest {

    @Autowired private FriendService friendService;
    @Autowired private WallService wallService;
    @Autowired private TestFixtures fixtures;

    // --- 好友 -----------------------------------------------------------------

    @Test
    @DisplayName("a friend request is pending until the addressee accepts")
    void friendRequestLifecycle() {
        User a = fixtures.consumer();
        User b = fixtures.consumer();

        friendService.sendRequest(a.getId(), b.getId());

        assertThat(friendService.listOutgoingRequests(a.getId())).hasSize(1);
        assertThat(friendService.listIncomingRequests(b.getId())).hasSize(1);
        assertThat(friendService.listFriends(a.getId())).isEmpty();

        Long friendshipId = friendService.listIncomingRequests(b.getId()).getFirst().friendshipId();
        friendService.respond(friendshipId, b.getId(), true);

        assertThat(friendService.listFriends(a.getId())).hasSize(1);
        assertThat(friendService.listFriends(b.getId())).hasSize(1);
    }

    /** Only the person who received the request may answer it. */
    @Test
    @DisplayName("the requester cannot accept their own request")
    void requesterCannotSelfAccept() {
        User a = fixtures.consumer();
        User b = fixtures.consumer();
        friendService.sendRequest(a.getId(), b.getId());
        Long friendshipId = friendService.listOutgoingRequests(a.getId()).getFirst().friendshipId();

        assertThatThrownBy(() -> friendService.respond(friendshipId, a.getId(), true))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("a duplicate request is rejected in either direction")
    void rejectsDuplicateRequests() {
        User a = fixtures.consumer();
        User b = fixtures.consumer();
        friendService.sendRequest(a.getId(), b.getId());

        assertThatThrownBy(() -> friendService.sendRequest(a.getId(), b.getId()))
                .isInstanceOf(ConflictException.class);
        // The legacy table allowed (a,b) and (b,a) to exist independently.
        assertThatThrownBy(() -> friendService.sendRequest(b.getId(), a.getId()))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("you cannot befriend yourself")
    void rejectsSelfFriending() {
        User a = fixtures.consumer();

        assertThatThrownBy(() -> friendService.sendRequest(a.getId(), a.getId()))
                .isInstanceOf(ValidationException.class);
    }

    // --- 留言牆 ---------------------------------------------------------------

    @Test
    @DisplayName("a message posted on someone's wall appears there")
    void postsToWall() {
        User owner = fixtures.consumer();
        User visitor = fixtures.consumer();

        wallService.post(
                owner.getId(), new PostWallMessageRequest("生日快樂！", null), fixtures.callerFor(visitor));

        var wall = wallService.getWall(owner.getId(), PageRequest.of(0, 10), fixtures.callerFor(owner));
        assertThat(wall.totalElements()).isEqualTo(1);
        assertThat(wall.content().getFirst().body()).isEqualTo("生日快樂！");
        assertThat(wall.content().getFirst().author().userId()).isEqualTo(visitor.getId());
    }

    @Test
    @DisplayName("replies nest exactly one level")
    void repliesNestOneLevel() {
        User owner = fixtures.consumer();
        User visitor = fixtures.consumer();

        Long parentId =
                wallService.post(owner.getId(), new PostWallMessageRequest("原留言", null), fixtures.callerFor(visitor));
        Long replyId = wallService.post(
                owner.getId(), new PostWallMessageRequest("回覆", parentId), fixtures.callerFor(owner));

        var wall = wallService.getWall(owner.getId(), PageRequest.of(0, 10), fixtures.callerFor(owner));
        assertThat(wall.totalElements()).isEqualTo(1); // the reply is not a top-level message
        assertThat(wall.content().getFirst().replies()).hasSize(1);

        assertThatThrownBy(() -> wallService.post(
                        owner.getId(), new PostWallMessageRequest("回覆的回覆", replyId), fixtures.callerFor(visitor)))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("a reply must belong to the same wall")
    void refusesCrossWallReply() {
        User ownerA = fixtures.consumer();
        User ownerB = fixtures.consumer();
        User visitor = fixtures.consumer();

        Long onWallA =
                wallService.post(ownerA.getId(), new PostWallMessageRequest("A 的留言", null), fixtures.callerFor(visitor));

        assertThatThrownBy(() -> wallService.post(
                        ownerB.getId(), new PostWallMessageRequest("跨牆回覆", onWallA), fixtures.callerFor(visitor)))
                .isInstanceOf(ValidationException.class);
    }

    /** Likes come from the join table, so they cannot drift as the legacy counter did. */
    @Test
    @DisplayName("liking is idempotent and reported per viewer")
    void likesAreIdempotent() {
        User owner = fixtures.consumer();
        User liker = fixtures.consumer();
        Long messageId =
                wallService.post(owner.getId(), new PostWallMessageRequest("按讚測試", null), fixtures.callerFor(owner));

        wallService.setLike(messageId, liker.getId(), true);
        wallService.setLike(messageId, liker.getId(), true);

        var asLiker = wallService.getWall(owner.getId(), PageRequest.of(0, 10), fixtures.callerFor(liker))
                .content().getFirst();
        assertThat(asLiker.likeCount()).isEqualTo(1);
        assertThat(asLiker.likedByMe()).isTrue();

        var asOwner = wallService.getWall(owner.getId(), PageRequest.of(0, 10), fixtures.callerFor(owner))
                .content().getFirst();
        assertThat(asOwner.likeCount()).isEqualTo(1);
        assertThat(asOwner.likedByMe()).isFalse();

        wallService.setLike(messageId, liker.getId(), false);
        assertThat(wallService.getWall(owner.getId(), PageRequest.of(0, 10), fixtures.callerFor(liker))
                        .content().getFirst().likeCount())
                .isZero();
    }

    @Test
    @DisplayName("the wall owner can delete someone else's message, a stranger cannot")
    void wallOwnerModerates() {
        User owner = fixtures.consumer();
        User visitor = fixtures.consumer();
        User stranger = fixtures.consumer();

        Long messageId = wallService.post(
                owner.getId(), new PostWallMessageRequest("請刪我", null), fixtures.callerFor(visitor));

        assertThatThrownBy(() -> wallService.delete(messageId, fixtures.callerFor(stranger)))
                .isInstanceOf(ForbiddenException.class);

        wallService.delete(messageId, fixtures.callerFor(owner));
        assertThat(wallService.getWall(owner.getId(), PageRequest.of(0, 10), fixtures.callerFor(owner))
                        .totalElements())
                .isZero();
    }

    // --- 追蹤 -----------------------------------------------------------------

    @Test
    @DisplayName("following is one-way and counted on both sides")
    void followIsOneWay() {
        User follower = fixtures.consumer();
        User followee = fixtures.consumer();

        wallService.follow(follower.getId(), followee.getId());

        assertThat(wallService.counts(followee.getId(), fixtures.callerFor(follower)))
                .satisfies(c -> {
                    assertThat(c.followers()).isEqualTo(1);
                    assertThat(c.followedByMe()).isTrue();
                });
        // The other direction is untouched.
        assertThat(wallService.counts(follower.getId(), fixtures.callerFor(followee)).followers()).isZero();

        assertThat(wallService.followers(followee.getId())).hasSize(1);
        assertThat(wallService.following(follower.getId())).hasSize(1);
    }

    @Test
    @DisplayName("following twice is rejected and following yourself is refused")
    void rejectsDuplicateAndSelfFollow() {
        User follower = fixtures.consumer();
        User followee = fixtures.consumer();
        wallService.follow(follower.getId(), followee.getId());

        assertThatThrownBy(() -> wallService.follow(follower.getId(), followee.getId()))
                .isInstanceOf(ConflictException.class);
        assertThatThrownBy(() -> wallService.follow(follower.getId(), follower.getId()))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("unfollowing is safe to repeat")
    void unfollowIsIdempotent() {
        User follower = fixtures.consumer();
        User followee = fixtures.consumer();
        wallService.follow(follower.getId(), followee.getId());

        wallService.unfollow(follower.getId(), followee.getId());
        wallService.unfollow(follower.getId(), followee.getId());

        assertThat(wallService.counts(followee.getId(), fixtures.callerFor(follower)).followers()).isZero();
    }
}
