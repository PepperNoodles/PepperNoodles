package com.peppernoodles.restaurant.api;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.restaurant.api.dto.RestaurantDetail;
import com.peppernoodles.restaurant.api.dto.RestaurantSummary;
import com.peppernoodles.restaurant.api.dto.ReviewDto;
import com.peppernoodles.restaurant.api.dto.SaveReplyRequest;
import com.peppernoodles.restaurant.api.dto.SaveRestaurantRequest;
import com.peppernoodles.restaurant.api.dto.SaveReviewRequest;
import com.peppernoodles.restaurant.service.RestaurantService;
import com.peppernoodles.restaurant.service.ReviewService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/restaurants")
@Tag(name = "Restaurants", description = "餐廳瀏覽、管理、評論與收藏")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    public RestaurantController(RestaurantService restaurantService, ReviewService reviewService) {
        this.restaurantService = restaurantService;
        this.reviewService = reviewService;
    }

    // --- public browsing -----------------------------------------------------

    @GetMapping
    @Operation(summary = "列出餐廳", description = "Optional free-text query or tag filter.")
    public PageResponse<RestaurantSummary> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) List<Long> tagIds,
            @PageableDefault(size = 20) Pageable pageable) {
        return restaurantService.list(q, tagIds, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "餐廳詳細資料")
    public RestaurantDetail get(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        return restaurantService.get(id, caller);
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "餐廳評論")
    public PageResponse<ReviewDto> reviews(
            @PathVariable Long id,
            @CurrentUser AuthenticatedUser caller,
            @PageableDefault(size = 10) Pageable pageable) {
        return reviewService.listForRestaurant(id, caller, pageable);
    }

    // --- owner CRUD ----------------------------------------------------------

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "新增餐廳", description = "企業會員限定。")
    public ResponseEntity<Void> create(
            @Valid @RequestBody SaveRestaurantRequest request, @CurrentUser AuthenticatedUser caller) {
        Long id = restaurantService.create(request, caller);
        return ResponseEntity.created(URI.create("/api/v1/restaurants/" + id)).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "修改餐廳", description = "Only the owning account or an administrator.")
    public void update(
            @PathVariable Long id,
            @Valid @RequestBody SaveRestaurantRequest request,
            @CurrentUser AuthenticatedUser caller) {
        restaurantService.update(id, request, caller);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除餐廳")
    public void delete(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        restaurantService.delete(id, caller);
    }

    @PostMapping(path = "/{id}/photo", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "上傳餐廳照片", description = "Stores the image in Supabase Storage and returns its URL.")
    public PhotoUploaded uploadPhoto(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @CurrentUser AuthenticatedUser caller) {
        return new PhotoUploaded(restaurantService.uploadPhoto(id, file, caller));
    }

    @PostMapping(path = "/{id}/menu", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "新增菜單照片")
    public void addMenuItem(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "caption", required = false) String caption,
            @CurrentUser AuthenticatedUser caller) {
        restaurantService.addMenuItem(id, file, caption, caller);
    }

    @DeleteMapping("/{id}/menu/{menuItemId}")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除菜單照片")
    public void deleteMenuItem(
            @PathVariable Long id, @PathVariable Long menuItemId, @CurrentUser AuthenticatedUser caller) {
        restaurantService.deleteMenuItem(id, menuItemId, caller);
    }

    // --- reviews -------------------------------------------------------------

    @PostMapping("/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "發表評論")
    public void createReview(
            @PathVariable Long id,
            @Valid @RequestBody SaveReviewRequest request,
            @CurrentUser AuthenticatedUser caller) {
        reviewService.create(id, request, caller);
    }

    @PutMapping("/reviews/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "修改評論", description = "Author only.")
    public void updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody SaveReviewRequest request,
            @CurrentUser AuthenticatedUser caller) {
        reviewService.update(reviewId, request, caller);
    }

    @DeleteMapping("/reviews/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除評論", description = "Author or administrator only.")
    public void deleteReview(@PathVariable Long reviewId, @CurrentUser AuthenticatedUser caller) {
        reviewService.delete(reviewId, caller);
    }

    @PostMapping("/reviews/{reviewId}/replies")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "回覆評論")
    public void replyToReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody SaveReplyRequest request,
            @CurrentUser AuthenticatedUser caller) {
        reviewService.reply(reviewId, request, caller);
    }

    @DeleteMapping("/reviews/replies/{replyId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除回覆", description = "Reply author, restaurant owner, or administrator.")
    public void deleteReply(@PathVariable Long replyId, @CurrentUser AuthenticatedUser caller) {
        reviewService.deleteReply(replyId, caller);
    }

    // --- favourites ----------------------------------------------------------

    @PutMapping("/{id}/favourite")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "加入收藏", description = "Idempotent.")
    public void addFavourite(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        restaurantService.addFavourite(id, caller.id());
    }

    @DeleteMapping("/{id}/favourite")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "取消收藏", description = "Idempotent.")
    public void removeFavourite(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        restaurantService.removeFavourite(id, caller.id());
    }

    public record PhotoUploaded(String photoUrl) {}
}
