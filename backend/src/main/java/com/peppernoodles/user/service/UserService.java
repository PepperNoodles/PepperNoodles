package com.peppernoodles.user.service;

import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.restaurant.api.dto.TagSummary;
import com.peppernoodles.tag.repository.FoodTagRepository;
import com.peppernoodles.user.api.dto.CompanyProfileDto;
import com.peppernoodles.user.api.dto.PublicProfileDto;
import com.peppernoodles.user.api.dto.UpdateCompanyProfileRequest;
import com.peppernoodles.user.api.dto.UpdateProfileRequest;
import com.peppernoodles.user.api.dto.UserProfileDto;
import com.peppernoodles.user.domain.CompanyProfile;
import com.peppernoodles.user.domain.Friendship;
import com.peppernoodles.user.domain.Role;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.domain.UserProfile;
import com.peppernoodles.user.domain.UserStats;
import com.peppernoodles.user.repository.FriendshipRepository;
import com.peppernoodles.user.repository.UserProfileRepository;
import com.peppernoodles.user.repository.UserRepository;
import com.peppernoodles.user.repository.UserStatsRepository;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/** 個人資料與企業資料維護. */
@Service
public class UserService {

    private final UserRepository users;
    private final UserProfileRepository profiles;
    private final UserStatsRepository stats;
    private final FriendshipRepository friendships;
    private final FoodTagRepository foodTags;
    private final StorageService storage;

    public UserService(
            UserRepository users,
            UserProfileRepository profiles,
            UserStatsRepository stats,
            FriendshipRepository friendships,
            FoodTagRepository foodTags,
            StorageService storage) {
        this.users = users;
        this.profiles = profiles;
        this.stats = stats;
        this.friendships = friendships;
        this.foodTags = foodTags;
        this.storage = storage;
    }

    @Transactional(readOnly = true)
    public UserProfileDto getOwnProfile(Long userId) {
        User user = users.findWithRolesById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));
        UserProfile profile = user.getProfile();

        return new UserProfileDto(
                user.getId(),
                user.getEmail(),
                profile == null ? null : profile.getRealName(),
                profile == null ? null : profile.getNickname(),
                profile == null ? null : profile.getPhone(),
                profile == null ? null : profile.getBirthDate(),
                profile == null ? null : profile.getGender(),
                profile == null ? null : profile.getLocation(),
                profile == null ? null : avatarUrl(profile.getAvatarPath()),
                toTags(user),
                user.getRoles().stream().map(Role::getName).toList(),
                stats.findByUserId(userId).map(UserService::toStats).orElse(null),
                user.getCreatedAt());
    }

    /** Another member's profile, with the caller's relationship to them. */
    @Transactional(readOnly = true)
    public PublicProfileDto getPublicProfile(Long targetUserId, AuthenticatedUser caller) {
        User user = users.findById(targetUserId).orElseThrow(() -> NotFoundException.of("使用者", targetUserId));
        UserProfile profile = user.getProfile();

        String friendship = null;
        if (caller != null && !caller.id().equals(targetUserId)) {
            friendship = friendships
                    .findBetween(caller.id(), targetUserId)
                    .map(f -> f.getStatus().name())
                    .orElse("NONE");
        }

        return new PublicProfileDto(
                user.getId(),
                profile == null ? "使用者" : profile.getNickname(),
                profile == null ? null : avatarUrl(profile.getAvatarPath()),
                profile == null ? null : profile.getLocation(),
                toTags(user),
                friendship);
    }

    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest request) {
        User user = users.findById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));

        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile();
            user.setProfile(profile);
        }

        if (request.realName() != null) profile.setRealName(request.realName());
        if (request.nickname() != null) profile.setNickname(request.nickname());
        if (request.phone() != null) profile.setPhone(request.phone());
        if (request.birthDate() != null) profile.setBirthDate(request.birthDate());
        if (request.gender() != null) profile.setGender(request.gender());
        if (request.location() != null) profile.setLocation(request.location());

        if (request.foodTagIds() != null) {
            user.setFoodTags(new LinkedHashSet<>(foodTags.findByIdIn(request.foodTagIds())));
        }
    }

    @Transactional
    public String uploadAvatar(Long userId, MultipartFile file) {
        User user = users.findById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));

        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile();
            user.setProfile(profile);
        }

        String previous = profile.getAvatarPath();
        String path = storage.upload(StorageBucket.USER_AVATARS, file);
        profile.setAvatarPath(path);
        storage.delete(StorageBucket.USER_AVATARS, previous);

        return avatarUrl(path);
    }

    @Transactional(readOnly = true)
    public CompanyProfileDto getCompanyProfile(Long userId) {
        User user = users.findById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));
        CompanyProfile profile = user.getCompanyProfile();
        if (profile == null) {
            throw new NotFoundException("此帳號沒有企業資料。");
        }
        return new CompanyProfileDto(
                user.getId(),
                user.getEmail(),
                profile.getRealName(),
                profile.getPhone(),
                profile.getLocation(),
                profile.getTier(),
                avatarUrl(profile.getAvatarPath()),
                profile.getCreatedAt());
    }

    @Transactional
    public void updateCompanyProfile(Long userId, UpdateCompanyProfileRequest request) {
        User user = users.findById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));
        CompanyProfile profile = user.getCompanyProfile();
        if (profile == null) {
            profile = new CompanyProfile();
            user.setCompanyProfile(profile);
        }
        profile.setRealName(request.companyName());
        profile.setPhone(request.phone());
        profile.setLocation(request.location());
    }

    /** Nickname search for the friend finder. */
    @Transactional(readOnly = true)
    public List<PublicProfileDto> searchByNickname(String nickname, AuthenticatedUser caller) {
        if (nickname == null || nickname.isBlank()) {
            return List.of();
        }
        return profiles.findTop20ByNicknameContainingIgnoreCase(nickname.trim()).stream()
                .map(p -> getPublicProfile(p.getUser().getId(), caller))
                .toList();
    }

    private List<TagSummary> toTags(User user) {
        return user.getFoodTags().stream()
                .map(TagSummary::from)
                .sorted(Comparator.comparing(TagSummary::name))
                .toList();
    }

    private String avatarUrl(String path) {
        return storage.publicUrl(StorageBucket.USER_AVATARS, path);
    }

    private static UserProfileDto.StatsDto toStats(UserStats s) {
        return new UserProfileDto.StatsDto(
                s.getTier(), s.getPostCount(), s.getLikeCount(), s.getFollowerCount(),
                s.getReplyCount(), s.getLoginCount(), s.getPurchaseCount());
    }
}
