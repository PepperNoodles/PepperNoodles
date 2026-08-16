package com.peppernoodles.restaurant.service;

import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.restaurant.api.dto.ReviewDto;
import com.peppernoodles.restaurant.api.dto.SaveReplyRequest;
import com.peppernoodles.restaurant.api.dto.SaveReviewRequest;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.domain.Review;
import com.peppernoodles.restaurant.domain.ReviewReply;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import com.peppernoodles.restaurant.repository.ReviewReplyRepository;
import com.peppernoodles.restaurant.repository.ReviewRepository;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Restaurant reviews and replies.
 *
 * <p>Authorship is always taken from the access token. The legacy endpoints
 * accepted the commenting user's id as a request parameter, so a crafted request
 * could post a review as anybody.
 */
@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviews;
    private final ReviewReplyRepository replies;
    private final RestaurantRepository restaurants;
    private final UserRepository users;
    private final RestaurantMapper mapper;

    public ReviewService(
            ReviewRepository reviews,
            ReviewReplyRepository replies,
            RestaurantRepository restaurants,
            UserRepository users,
            RestaurantMapper mapper) {
        this.reviews = reviews;
        this.replies = replies;
        this.restaurants = restaurants;
        this.users = users;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public PageResponse<ReviewDto> listForRestaurant(Long restaurantId, AuthenticatedUser caller, Pageable pageable) {
        Restaurant restaurant =
                restaurants.findById(restaurantId).orElseThrow(() -> NotFoundException.of("餐廳", restaurantId));
        Long ownerId = restaurant.getOwner().getId();
        Long callerId = caller == null ? null : caller.id();

        return PageResponse.of(
                reviews.findByRestaurantIdOrderByCreatedAtDesc(restaurantId, pageable),
                review -> mapper.toReview(review, callerId, ownerId));
    }

    @Transactional
    public Long create(Long restaurantId, SaveReviewRequest request, AuthenticatedUser caller) {
        Restaurant restaurant =
                restaurants.findById(restaurantId).orElseThrow(() -> NotFoundException.of("餐廳", restaurantId));
        User author = users.findById(caller.id()).orElseThrow(() -> NotFoundException.of("使用者", caller.id()));

        Review review = new Review(restaurant, author, request.body(), request.score());
        Long id = reviews.save(review).getId();
        log.info("User {} reviewed restaurant {}", caller.id(), restaurantId);
        return id;
    }

    @Transactional
    public void update(Long reviewId, SaveReviewRequest request, AuthenticatedUser caller) {
        Review review = reviews.findById(reviewId).orElseThrow(() -> NotFoundException.of("評論", reviewId));
        requireAuthorOrAdmin(review.isAuthoredBy(caller.id()), caller, "評論");

        review.setBody(request.body());
        review.setScore(request.score());
    }

    @Transactional
    public void delete(Long reviewId, AuthenticatedUser caller) {
        Review review = reviews.findById(reviewId).orElseThrow(() -> NotFoundException.of("評論", reviewId));
        requireAuthorOrAdmin(review.isAuthoredBy(caller.id()), caller, "評論");
        reviews.delete(review);
    }

    @Transactional
    public Long reply(Long reviewId, SaveReplyRequest request, AuthenticatedUser caller) {
        Review review = reviews.findById(reviewId).orElseThrow(() -> NotFoundException.of("評論", reviewId));
        User author = users.findById(caller.id()).orElseThrow(() -> NotFoundException.of("使用者", caller.id()));

        ReviewReply reply = new ReviewReply(author, request.body());
        review.addReply(reply);
        reviews.save(review);
        return reply.getId();
    }

    @Transactional
    public void deleteReply(Long replyId, AuthenticatedUser caller) {
        ReviewReply reply = replies.findById(replyId).orElseThrow(() -> NotFoundException.of("回覆", replyId));

        // The reply's author may delete it, and so may the restaurant's owner —
        // owners need to be able to remove abuse from their own review threads.
        boolean ownsRestaurant =
                reply.getReview().getRestaurant().isOwnedBy(caller.id());
        requireAuthorOrAdmin(reply.isAuthoredBy(caller.id()) || ownsRestaurant, caller, "回覆");

        replies.delete(reply);
    }

    private static void requireAuthorOrAdmin(boolean permitted, AuthenticatedUser caller, String what) {
        if (!permitted && !caller.isAdmin()) {
            throw new ForbiddenException("您沒有權限修改這則%s。".formatted(what));
        }
    }
}
