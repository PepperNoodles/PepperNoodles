package com.peppernoodles.restaurant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.restaurant.api.dto.SaveReplyRequest;
import com.peppernoodles.restaurant.api.dto.SaveReviewRequest;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.service.RestaurantService;
import com.peppernoodles.restaurant.service.ReviewService;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.domain.User;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class ReviewServiceTest extends IntegrationTest {

    @Autowired private ReviewService reviewService;
    @Autowired private RestaurantService restaurantService;
    @Autowired private TestFixtures fixtures;

    @Test
    @DisplayName("a diner can review a restaurant and read it back")
    void createsReview() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        User diner = fixtures.consumer();

        reviewService.create(
                restaurant.getId(), new SaveReviewRequest("很好吃", (short) 5), fixtures.callerFor(diner));

        var page = reviewService.listForRestaurant(
                restaurant.getId(), fixtures.callerFor(diner), PageRequest.of(0, 10));
        assertThat(page.totalElements()).isEqualTo(1);
        assertThat(page.content().getFirst().body()).isEqualTo("很好吃");
        assertThat(page.content().getFirst().editable()).isTrue();
    }

    /** The legacy code let anyone delete any review. */
    @Test
    @DisplayName("only the author or an admin can delete a review")
    void refusesDeleteByStranger() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        User author = fixtures.consumer();
        User stranger = fixtures.consumer();

        Long reviewId = reviewService.create(
                restaurant.getId(), new SaveReviewRequest("普通", (short) 3), fixtures.callerFor(author));

        assertThatThrownBy(() -> reviewService.delete(reviewId, fixtures.callerFor(stranger)))
                .isInstanceOf(ForbiddenException.class);

        reviewService.delete(reviewId, fixtures.callerFor(fixtures.admin()));
        assertThat(reviewService
                        .listForRestaurant(restaurant.getId(), fixtures.callerFor(author), PageRequest.of(0, 10))
                        .totalElements())
                .isZero();
    }

    @Test
    @DisplayName("a review shows the restaurant owner's reply as coming from the shop")
    void marksOwnerReplies() {
        User owner = fixtures.owner();
        Restaurant restaurant = fixtures.restaurant(owner);
        User diner = fixtures.consumer();

        Long reviewId = reviewService.create(
                restaurant.getId(), new SaveReviewRequest("有點鹹", (short) 3), fixtures.callerFor(diner));
        reviewService.reply(reviewId, new SaveReplyRequest("感謝指教，我們會改進"), fixtures.callerFor(owner));

        var review = reviewService
                .listForRestaurant(restaurant.getId(), fixtures.callerFor(diner), PageRequest.of(0, 10))
                .content()
                .getFirst();

        assertThat(review.replies()).hasSize(1);
        assertThat(review.replies().getFirst().fromRestaurantOwner()).isTrue();
    }

    @Test
    @DisplayName("a reply from another diner is not marked as the shop")
    void doesNotMarkDinerRepliesAsOwner() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        User diner = fixtures.consumer();
        User other = fixtures.consumer();

        Long reviewId = reviewService.create(
                restaurant.getId(), new SaveReviewRequest("還行", (short) 4), fixtures.callerFor(diner));
        reviewService.reply(reviewId, new SaveReplyRequest("我也這麼覺得"), fixtures.callerFor(other));

        var review = reviewService
                .listForRestaurant(restaurant.getId(), fixtures.callerFor(diner), PageRequest.of(0, 10))
                .content()
                .getFirst();

        assertThat(review.replies().getFirst().fromRestaurantOwner()).isFalse();
    }

    /**
     * The legacy schema kept totalScore and a *string* review count on the
     * restaurant row, updated by hand from several controllers. The rating is
     * now derived, so it cannot drift.
     */
    @Test
    @DisplayName("the rating average is derived from the reviews")
    void derivesRatingFromReviews() {
        User owner = fixtures.owner();
        Restaurant restaurant = fixtures.restaurant(owner);

        reviewService.create(
                restaurant.getId(), new SaveReviewRequest("讚", (short) 5), fixtures.callerFor(fixtures.consumer()));
        reviewService.create(
                restaurant.getId(), new SaveReviewRequest("普通", (short) 3), fixtures.callerFor(fixtures.consumer()));

        var rating = restaurantService.get(restaurant.getId(), fixtures.callerFor(owner)).rating();

        assertThat(rating.reviewCount()).isEqualTo(2);
        assertThat(rating.average()).isEqualByComparingTo(new BigDecimal("4.00"));
    }

    @Test
    @DisplayName("a review without a score still counts as a review but not as a rating")
    void allowsUnscoredReviews() {
        User owner = fixtures.owner();
        Restaurant restaurant = fixtures.restaurant(owner);

        reviewService.create(
                restaurant.getId(), new SaveReviewRequest("只留言不評分", null), fixtures.callerFor(fixtures.consumer()));

        var rating = restaurantService.get(restaurant.getId(), fixtures.callerFor(owner)).rating();
        assertThat(rating.reviewCount()).isEqualTo(1);
        assertThat(rating.ratingCount()).isZero();
    }
}
