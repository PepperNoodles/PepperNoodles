package com.peppernoodles.user.api;

import com.peppernoodles.auth.service.AuthService;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.restaurant.api.dto.RestaurantSummary;
import com.peppernoodles.restaurant.service.RestaurantService;
import com.peppernoodles.user.api.dto.ChangePasswordRequest;
import com.peppernoodles.user.api.dto.CompanyProfileDto;
import com.peppernoodles.user.api.dto.PublicProfileDto;
import com.peppernoodles.user.api.dto.UpdateCompanyProfileRequest;
import com.peppernoodles.user.api.dto.UpdateProfileRequest;
import com.peppernoodles.user.api.dto.UserProfileDto;
import com.peppernoodles.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "個人資料、企業資料、收藏")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final RestaurantService restaurantService;

    public UserController(
            UserService userService, AuthService authService, RestaurantService restaurantService) {
        this.userService = userService;
        this.authService = authService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "取得自己的個人資料")
    public UserProfileDto me(@CurrentUser AuthenticatedUser caller) {
        return userService.getOwnProfile(caller.id());
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "修改個人資料")
    public void updateMe(
            @Valid @RequestBody UpdateProfileRequest request, @CurrentUser AuthenticatedUser caller) {
        userService.updateProfile(caller.id(), request);
    }

    @PostMapping(path = "/me/avatar", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "上傳大頭貼")
    public AvatarUploaded uploadAvatar(
            @RequestPart("file") MultipartFile file, @CurrentUser AuthenticatedUser caller) {
        return new AvatarUploaded(userService.uploadAvatar(caller.id(), file));
    }

    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "修改密碼", description = "Revokes every other session on success.")
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest request, @CurrentUser AuthenticatedUser caller) {
        authService.changePassword(caller.id(), request.currentPassword(), request.newPassword());
    }

    @GetMapping("/me/company")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "取得企業資料")
    public CompanyProfileDto companyProfile(@CurrentUser AuthenticatedUser caller) {
        return userService.getCompanyProfile(caller.id());
    }

    @PutMapping("/me/company")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "修改企業資料")
    public void updateCompanyProfile(
            @Valid @RequestBody UpdateCompanyProfileRequest request, @CurrentUser AuthenticatedUser caller) {
        userService.updateCompanyProfile(caller.id(), request);
    }

    @GetMapping("/me/favourites")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "我的餐廳收藏")
    public PageResponse<RestaurantSummary> favourites(
            @CurrentUser AuthenticatedUser caller, @PageableDefault(size = 20) Pageable pageable) {
        return restaurantService.listFavouritesOf(caller.id(), pageable);
    }

    @GetMapping("/me/restaurants")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "我經營的餐廳")
    public PageResponse<RestaurantSummary> myRestaurants(
            @CurrentUser AuthenticatedUser caller, @PageableDefault(size = 20) Pageable pageable) {
        return restaurantService.listOwnedBy(caller.id(), pageable);
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "以暱稱搜尋會員")
    public List<PublicProfileDto> search(
            @RequestParam String nickname, @CurrentUser AuthenticatedUser caller) {
        return userService.searchByNickname(nickname, caller);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "其他會員的公開資料")
    public PublicProfileDto publicProfile(
            @PathVariable Long userId, @CurrentUser AuthenticatedUser caller) {
        return userService.getPublicProfile(userId, caller);
    }

    public record AvatarUploaded(String avatarUrl) {}
}
