package com.peppernoodles.user.api.dto;

import com.peppernoodles.restaurant.api.dto.TagSummary;
import com.peppernoodles.user.domain.UserProfile.Gender;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/** 個人資料. The caller's own profile, or another member's public view. */
public record UserProfileDto(
        Long userId,
        String email,
        String realName,
        String nickname,
        String phone,
        LocalDate birthDate,
        Gender gender,
        String location,
        String avatarUrl,
        List<TagSummary> foodTags,
        List<String> roles,
        StatsDto stats,
        Instant createdAt) {

    public record StatsDto(
            String tier, int postCount, int likeCount, int followerCount,
            int replyCount, int loginCount, int purchaseCount) {}
}
