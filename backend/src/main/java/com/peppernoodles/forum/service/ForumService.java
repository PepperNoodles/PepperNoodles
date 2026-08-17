package com.peppernoodles.forum.service;

import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.forum.api.dto.ForumDtos.*;
import com.peppernoodles.forum.domain.ForumComment;
import com.peppernoodles.forum.domain.ForumCommentReply;
import com.peppernoodles.forum.domain.ForumPost;
import com.peppernoodles.forum.repository.ForumCommentReplyRepository;
import com.peppernoodles.forum.repository.ForumCommentRepository;
import com.peppernoodles.forum.repository.ForumPostRepository;
import com.peppernoodles.restaurant.api.dto.TagSummary;
import com.peppernoodles.tag.repository.FoodTagRepository;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.domain.UserProfile;
import com.peppernoodles.user.repository.UserRepository;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/** 論壇 / 專欄文章. */
@Service
public class ForumService {

    private static final int EXCERPT_LENGTH = 140;

    private final ForumPostRepository posts;
    private final ForumCommentRepository comments;
    private final ForumCommentReplyRepository replies;
    private final FoodTagRepository foodTags;
    private final UserRepository users;
    private final StorageService storage;

    public ForumService(
            ForumPostRepository posts,
            ForumCommentRepository comments,
            ForumCommentReplyRepository replies,
            FoodTagRepository foodTags,
            UserRepository users,
            StorageService storage) {
        this.posts = posts;
        this.comments = comments;
        this.replies = replies;
        this.foodTags = foodTags;
        this.users = users;
        this.storage = storage;
    }

    // --- posts ---------------------------------------------------------------

    @Transactional(readOnly = true)
    public PageResponse<PostSummary> list(List<Long> tagIds, Long authorId, Pageable pageable, AuthenticatedUser caller) {
        Page<ForumPost> page;
        if (tagIds != null && !tagIds.isEmpty()) {
            page = posts.findByTagIds(tagIds, pageable);
        } else if (authorId != null) {
            page = posts.findByAuthorIdOrderByCreatedAtDesc(authorId, pageable);
        } else {
            page = posts.findAllByOrderByCreatedAtDesc(pageable);
        }
        return PageResponse.of(page, post -> toSummary(post, caller));
    }

    @Transactional(readOnly = true)
    public PageResponse<PostSummary> listBookmarked(Long userId, Pageable pageable, AuthenticatedUser caller) {
        return PageResponse.of(posts.findBookmarkedBy(userId, pageable), post -> toSummary(post, caller));
    }

    @Transactional(readOnly = true)
    public PostDetail get(Long id, AuthenticatedUser caller) {
        ForumPost post = posts.findDetailedById(id).orElseThrow(() -> NotFoundException.of("文章", id));

        List<CommentDto> commentDtos = comments.findByPostIdOrderByCreatedAtAsc(id).stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        toAuthor(comment.getAuthor()),
                        comment.getBody(),
                        comment.getScore(),
                        canEdit(comment.isAuthoredBy(callerId(caller)), caller),
                        comment.getCreatedAt(),
                        comment.getReplies().stream()
                                .map(reply -> new ReplyDto(
                                        reply.getId(),
                                        toAuthor(reply.getAuthor()),
                                        reply.getReplyToUser() == null ? null : toAuthor(reply.getReplyToUser()),
                                        reply.getBody(),
                                        canEdit(reply.isAuthoredBy(callerId(caller)), caller),
                                        reply.getCreatedAt()))
                                .toList()))
                .toList();

        return new PostDetail(
                post.getId(),
                toAuthor(post.getAuthor()),
                post.getBody(),
                storage.publicUrl(StorageBucket.FORUM_IMAGES, post.getImagePath()),
                tagsOf(post),
                posts.countBookmarks(post.getId()),
                caller != null && posts.isBookmarkedBy(post.getId(), caller.id()),
                canEdit(post.isAuthoredBy(callerId(caller)), caller),
                commentDtos,
                post.getCreatedAt(),
                post.getUpdatedAt());
    }

    @Transactional
    public Long create(SavePostRequest request, AuthenticatedUser caller) {
        User author = users.findById(caller.id()).orElseThrow(() -> NotFoundException.of("使用者", caller.id()));
        ForumPost post = new ForumPost(author, request.body());
        applyTags(post, request.tagIds());
        return posts.save(post).getId();
    }

    @Transactional
    public void update(Long id, SavePostRequest request, AuthenticatedUser caller) {
        ForumPost post = posts.findById(id).orElseThrow(() -> NotFoundException.of("文章", id));
        requireAuthor(post.isAuthoredBy(caller.id()), caller);
        post.setBody(request.body());
        applyTags(post, request.tagIds());
    }

    @Transactional
    public void delete(Long id, AuthenticatedUser caller) {
        ForumPost post = posts.findById(id).orElseThrow(() -> NotFoundException.of("文章", id));
        requireAuthor(post.isAuthoredBy(caller.id()), caller);
        String image = post.getImagePath();
        posts.delete(post);
        storage.delete(StorageBucket.FORUM_IMAGES, image);
    }

    @Transactional
    public String uploadImage(Long id, MultipartFile file, AuthenticatedUser caller) {
        ForumPost post = posts.findById(id).orElseThrow(() -> NotFoundException.of("文章", id));
        requireAuthor(post.isAuthoredBy(caller.id()), caller);

        String previous = post.getImagePath();
        String path = storage.upload(StorageBucket.FORUM_IMAGES, file);
        post.setImagePath(path);
        storage.delete(StorageBucket.FORUM_IMAGES, previous);
        return storage.publicUrl(StorageBucket.FORUM_IMAGES, path);
    }

    /** 收藏 — idempotent in both directions. */
    @Transactional
    public void setBookmark(Long postId, Long userId, boolean bookmarked) {
        ForumPost post = posts.findById(postId).orElseThrow(() -> NotFoundException.of("文章", postId));
        User user = users.findById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));
        if (bookmarked) {
            post.getBookmarkedBy().add(user);
        } else {
            post.getBookmarkedBy().remove(user);
        }
    }

    // --- comments and replies ------------------------------------------------

    @Transactional
    public Long addComment(Long postId, SaveCommentRequest request, AuthenticatedUser caller) {
        ForumPost post = posts.findById(postId).orElseThrow(() -> NotFoundException.of("文章", postId));
        User author = users.findById(caller.id()).orElseThrow(() -> NotFoundException.of("使用者", caller.id()));
        return comments.save(new ForumComment(post, author, request.body(), request.score())).getId();
    }

    @Transactional
    public void deleteComment(Long commentId, AuthenticatedUser caller) {
        ForumComment comment =
                comments.findDetailedById(commentId).orElseThrow(() -> NotFoundException.of("留言", commentId));
        // The post's author may also moderate their own thread.
        boolean allowed = comment.isAuthoredBy(caller.id()) || comment.getPost().isAuthoredBy(caller.id());
        requireAuthor(allowed, caller);
        comments.delete(comment);
    }

    @Transactional
    public Long addReply(Long commentId, SaveReplyRequest request, AuthenticatedUser caller) {
        ForumComment comment =
                comments.findById(commentId).orElseThrow(() -> NotFoundException.of("留言", commentId));
        User author = users.findById(caller.id()).orElseThrow(() -> NotFoundException.of("使用者", caller.id()));
        User replyTo = request.replyToUserId() == null ? null : users.findById(request.replyToUserId()).orElse(null);
        return replies.save(new ForumCommentReply(comment, author, replyTo, request.body())).getId();
    }

    @Transactional
    public void deleteReply(Long replyId, AuthenticatedUser caller) {
        ForumCommentReply reply =
                replies.findDetailedById(replyId).orElseThrow(() -> NotFoundException.of("回覆", replyId));
        boolean allowed = reply.isAuthoredBy(caller.id()) || reply.getComment().getPost().isAuthoredBy(caller.id());
        requireAuthor(allowed, caller);
        replies.delete(reply);
    }

    // --- helpers -------------------------------------------------------------

    private PostSummary toSummary(ForumPost post, AuthenticatedUser caller) {
        String body = post.getBody();
        return new PostSummary(
                post.getId(),
                toAuthor(post.getAuthor()),
                body.length() > EXCERPT_LENGTH ? body.substring(0, EXCERPT_LENGTH) + "…" : body,
                storage.publicUrl(StorageBucket.FORUM_IMAGES, post.getImagePath()),
                tagsOf(post),
                posts.countComments(post.getId()),
                posts.countBookmarks(post.getId()),
                caller != null && posts.isBookmarkedBy(post.getId(), caller.id()),
                canEdit(post.isAuthoredBy(callerId(caller)), caller),
                post.getCreatedAt());
    }

    private List<TagSummary> tagsOf(ForumPost post) {
        return post.getTags().stream().map(TagSummary::from).toList();
    }

    private void applyTags(ForumPost post, List<Long> tagIds) {
        if (tagIds != null) {
            post.setTags(new LinkedHashSet<>(foodTags.findByIdIn(tagIds)));
        }
    }

    private AuthorDto toAuthor(User user) {
        UserProfile profile = user.getProfile();
        return new AuthorDto(
                user.getId(),
                profile == null || profile.getNickname() == null ? "會員" : profile.getNickname(),
                profile == null ? null : storage.publicUrl(StorageBucket.USER_AVATARS, profile.getAvatarPath()));
    }

    private static Long callerId(AuthenticatedUser caller) {
        return caller == null ? null : caller.id();
    }

    private static boolean canEdit(boolean isAuthor, AuthenticatedUser caller) {
        return caller != null && (isAuthor || caller.isAdmin());
    }

    private static void requireAuthor(boolean isAuthor, AuthenticatedUser caller) {
        if (!canEdit(isAuthor, caller)) {
            throw new ForbiddenException("您沒有權限修改這則內容。");
        }
    }
}
