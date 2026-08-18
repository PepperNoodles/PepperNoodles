package com.peppernoodles.forum;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.forum.api.dto.ForumDtos.SaveCommentRequest;
import com.peppernoodles.forum.api.dto.ForumDtos.SavePostRequest;
import com.peppernoodles.forum.api.dto.ForumDtos.SaveReplyRequest;
import com.peppernoodles.forum.service.ForumService;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/** 論壇 / 專欄文章. */
class ForumServiceTest extends IntegrationTest {

    @Autowired private ForumService forumService;
    @Autowired private TestFixtures fixtures;

    private Long post(User author, String body) {
        return forumService.create(new SavePostRequest(body, List.of()), fixtures.callerFor(author));
    }

    @Test
    @DisplayName("a post can be written and read back with its author")
    void createsPost() {
        User author = fixtures.consumer();
        Long id = post(author, "今天吃了很棒的滷肉飯");

        var detail = forumService.get(id, fixtures.callerFor(author));
        assertThat(detail.body()).isEqualTo("今天吃了很棒的滷肉飯");
        assertThat(detail.author().userId()).isEqualTo(author.getId());
        assertThat(detail.editable()).isTrue();
    }

    @Test
    @DisplayName("the list shows a truncated excerpt, not the whole body")
    void listShowsExcerpt() {
        User author = fixtures.consumer();
        String longBody = "很".repeat(400);
        post(author, longBody);

        var summary = forumService
                .list(null, author.getId(), PageRequest.of(0, 10), fixtures.callerFor(author))
                .content()
                .getFirst();

        assertThat(summary.excerpt()).hasSizeLessThan(longBody.length());
        assertThat(summary.excerpt()).endsWith("…");
    }

    @Test
    @DisplayName("only the author or an admin can edit or delete a post")
    void enforcesPostOwnership() {
        User author = fixtures.consumer();
        User stranger = fixtures.consumer();
        Long id = post(author, "我的文章");

        assertThatThrownBy(() -> forumService.update(
                        id, new SavePostRequest("被改掉", List.of()), fixtures.callerFor(stranger)))
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> forumService.delete(id, fixtures.callerFor(stranger)))
                .isInstanceOf(ForbiddenException.class);

        forumService.delete(id, fixtures.callerFor(fixtures.admin()));
        assertThatThrownBy(() -> forumService.get(id, fixtures.callerFor(author)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("comments and replies are returned nested under the post")
    void nestsCommentsAndReplies() {
        User author = fixtures.consumer();
        User commenter = fixtures.consumer();
        Long postId = post(author, "求推薦宵夜");

        Long commentId = forumService.addComment(
                postId, new SaveCommentRequest("推薦鹹酥雞", (short) 5), fixtures.callerFor(commenter));
        forumService.addReply(
                commentId, new SaveReplyRequest("同意！", commenter.getId()), fixtures.callerFor(author));

        var detail = forumService.get(postId, fixtures.callerFor(author));
        assertThat(detail.comments()).hasSize(1);

        var comment = detail.comments().getFirst();
        assertThat(comment.score()).isEqualTo((short) 5);
        assertThat(comment.replies()).hasSize(1);
        // The legacy row kept both the replier and the person replied to.
        assertThat(comment.replies().getFirst().replyTo().userId()).isEqualTo(commenter.getId());
        assertThat(comment.replies().getFirst().author().userId()).isEqualTo(author.getId());
    }

    /** A thread's owner moderates it, as in the legacy forum. */
    @Test
    @DisplayName("the post's author can delete someone else's comment on their thread")
    void postAuthorModeratesThread() {
        User author = fixtures.consumer();
        User commenter = fixtures.consumer();
        User stranger = fixtures.consumer();
        Long postId = post(author, "我的討論串");
        Long commentId = forumService.addComment(
                postId, new SaveCommentRequest("路過留言", null), fixtures.callerFor(commenter));

        assertThatThrownBy(() -> forumService.deleteComment(commentId, fixtures.callerFor(stranger)))
                .isInstanceOf(ForbiddenException.class);

        forumService.deleteComment(commentId, fixtures.callerFor(author));
        assertThat(forumService.get(postId, fixtures.callerFor(author)).comments()).isEmpty();
    }

    @Test
    @DisplayName("deleting a comment removes its replies with it")
    void deletingCommentRemovesReplies() {
        User author = fixtures.consumer();
        Long postId = post(author, "串接測試");
        Long commentId =
                forumService.addComment(postId, new SaveCommentRequest("留言", null), fixtures.callerFor(author));
        forumService.addReply(commentId, new SaveReplyRequest("回覆", null), fixtures.callerFor(author));

        forumService.deleteComment(commentId, fixtures.callerFor(author));

        assertThat(forumService.get(postId, fixtures.callerFor(author)).comments()).isEmpty();
    }

    @Test
    @DisplayName("bookmarking is idempotent and per-viewer")
    void bookmarksAreIdempotent() {
        User author = fixtures.consumer();
        User reader = fixtures.consumer();
        Long postId = post(author, "值得收藏的文章");

        forumService.setBookmark(postId, reader.getId(), true);
        forumService.setBookmark(postId, reader.getId(), true);

        var asReader = forumService.get(postId, fixtures.callerFor(reader));
        assertThat(asReader.bookmarkCount()).isEqualTo(1);
        assertThat(asReader.bookmarked()).isTrue();

        assertThat(forumService.get(postId, fixtures.callerFor(author)).bookmarked()).isFalse();

        assertThat(forumService
                        .listBookmarked(reader.getId(), PageRequest.of(0, 10), fixtures.callerFor(reader))
                        .totalElements())
                .isEqualTo(1);

        forumService.setBookmark(postId, reader.getId(), false);
        assertThat(forumService.get(postId, fixtures.callerFor(reader)).bookmarkCount()).isZero();
    }

    @Test
    @DisplayName("the comment count on the list matches the thread")
    void reportsCommentCount() {
        User author = fixtures.consumer();
        Long postId = post(author, "計數測試");
        forumService.addComment(postId, new SaveCommentRequest("一", null), fixtures.callerFor(author));
        forumService.addComment(postId, new SaveCommentRequest("二", null), fixtures.callerFor(author));

        var summary = forumService
                .list(null, author.getId(), PageRequest.of(0, 10), fixtures.callerFor(author))
                .content()
                .getFirst();

        assertThat(summary.commentCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("posts can be filtered by tag")
    void filtersByTag() {
        User author = fixtures.consumer();
        var tag = fixtures.anyTag();

        Long tagged = forumService.create(
                new SavePostRequest("有標籤的文章", List.of(tag.getId())), fixtures.callerFor(author));
        Long untagged = post(author, "沒有標籤的文章");

        var ids = forumService
                .list(List.of(tag.getId()), null, PageRequest.of(0, 100), fixtures.callerFor(author))
                .content()
                .stream()
                .map(p -> p.id())
                .toList();

        assertThat(ids).contains(tagged);
        assertThat(ids).doesNotContain(untagged);
    }

    @Test
    @DisplayName("an anonymous reader sees the post but nothing marked editable")
    void anonymousReaderSeesReadOnlyView() {
        User author = fixtures.consumer();
        Long postId = post(author, "公開文章");
        forumService.addComment(postId, new SaveCommentRequest("留言", null), fixtures.callerFor(author));

        var detail = forumService.get(postId, null);

        assertThat(detail.body()).isEqualTo("公開文章");
        assertThat(detail.editable()).isFalse();
        assertThat(detail.bookmarked()).isFalse();
        assertThat(detail.comments().getFirst().editable()).isFalse();
    }
}
