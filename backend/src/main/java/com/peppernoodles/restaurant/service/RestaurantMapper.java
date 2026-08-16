package com.peppernoodles.restaurant.service;

import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.restaurant.api.dto.BusinessHourDto;
import com.peppernoodles.restaurant.api.dto.MenuItemDto;
import com.peppernoodles.restaurant.api.dto.RatingSummary;
import com.peppernoodles.restaurant.api.dto.RestaurantEventDto;
import com.peppernoodles.restaurant.api.dto.RestaurantSummary;
import com.peppernoodles.restaurant.api.dto.ReviewDto;
import com.peppernoodles.restaurant.api.dto.TagSummary;
import com.peppernoodles.restaurant.domain.MenuItem;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.domain.RestaurantEvent;
import com.peppernoodles.restaurant.domain.Review;
import com.peppernoodles.restaurant.domain.ReviewReply;
import com.peppernoodles.restaurant.repository.RestaurantRatingRepository.RatingRow;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.domain.UserProfile;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Entity → DTO conversion for the restaurant domain.
 *
 * <p>Kept in one place so that entities never reach Jackson. The legacy code
 * serialised entities directly and controlled the output with a scattering of
 * {@code @JsonIgnore} annotations, which meant a new association could silently
 * start leaking data — or trigger a lazy-loading exception — on an unrelated
 * endpoint.
 */
@Component
public class RestaurantMapper {

    private final StorageService storage;

    public RestaurantMapper(StorageService storage) {
        this.storage = storage;
    }

    public RestaurantSummary toSummary(Restaurant restaurant, RatingSummary rating, Double distanceMetres) {
        return new RestaurantSummary(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getContact(),
                restaurant.getWebsite(),
                photoUrl(restaurant.getPhotoPath()),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                rating,
                toTags(restaurant),
                distanceMetres);
    }

    public List<TagSummary> toTags(Restaurant restaurant) {
        return restaurant.getFoodTags().stream()
                .map(TagSummary::from)
                .sorted(Comparator.comparing(TagSummary::name))
                .toList();
    }

    public List<BusinessHourDto> toBusinessHours(Restaurant restaurant) {
        return restaurant.getBusinessHours().stream().map(BusinessHourDto::from).toList();
    }

    public MenuItemDto toMenuItem(MenuItem item) {
        return new MenuItemDto(
                item.getId(),
                item.getCaption(),
                storage.publicUrl(StorageBucket.MENU_PHOTOS, item.getImagePath()),
                item.getPosition());
    }

    public RestaurantEventDto toEvent(RestaurantEvent event) {
        return new RestaurantEventDto(
                event.getId(),
                event.getRestaurant().getId(),
                event.getName(),
                event.getContent(),
                storage.publicUrl(StorageBucket.EVENT_PHOTOS, event.getImagePath()),
                event.getStartsOn(),
                event.getEndsOn(),
                event.isActiveOn(LocalDate.now()));
    }

    public ReviewDto toReview(Review review, Long callerId, Long restaurantOwnerId) {
        List<ReviewDto.ReplyDto> replies = review.getReplies().stream()
                .map(reply -> toReply(reply, callerId, restaurantOwnerId))
                .toList();

        return new ReviewDto(
                review.getId(),
                toAuthor(review.getAuthor()),
                review.getBody(),
                review.getScore(),
                review.getCreatedAt(),
                replies,
                callerId != null && review.isAuthoredBy(callerId));
    }

    private ReviewDto.ReplyDto toReply(ReviewReply reply, Long callerId, Long restaurantOwnerId) {
        Long authorId = reply.getAuthor().getId();
        return new ReviewDto.ReplyDto(
                reply.getId(),
                toAuthor(reply.getAuthor()),
                reply.getBody(),
                reply.getCreatedAt(),
                authorId.equals(restaurantOwnerId),
                callerId != null && authorId.equals(callerId));
    }

    public ReviewDto.AuthorSummary toAuthor(User user) {
        UserProfile profile = user.getProfile();
        String displayName;
        String avatarUrl = null;

        if (profile != null) {
            displayName = profile.getNickname() != null ? profile.getNickname() : profile.getRealName();
            avatarUrl = storage.publicUrl(StorageBucket.USER_AVATARS, profile.getAvatarPath());
        } else if (user.getCompanyProfile() != null) {
            displayName = user.getCompanyProfile().getRealName();
            avatarUrl = storage.publicUrl(StorageBucket.USER_AVATARS, user.getCompanyProfile().getAvatarPath());
        } else {
            displayName = "使用者";
        }

        return new ReviewDto.AuthorSummary(user.getId(), displayName, avatarUrl);
    }

    public String photoUrl(String photoPath) {
        return storage.publicUrl(StorageBucket.RESTAURANT_PHOTOS, photoPath);
    }

    public static RatingSummary toRating(RatingRow row) {
        if (row == null) {
            return RatingSummary.NONE;
        }
        return new RatingSummary(
                row.getRatingAverage(),
                row.getRatingCount() == null ? 0 : row.getRatingCount(),
                row.getReviewCount() == null ? 0 : row.getReviewCount());
    }
}
