package com.peppernoodles.user.api.dto;

import com.peppernoodles.restaurant.api.dto.TagSummary;
import java.util.List;

/**
 * Another member's profile as seen by a third party.
 *
 * <p>Deliberately omits e-mail, phone, birth date, and exact location. The
 * legacy endpoints returned the whole {@code UserAccount} entity, so a member's
 * phone number and BCrypt hash were reachable from the friend list.
 */
public record PublicProfileDto(
        Long userId,
        String nickname,
        String avatarUrl,
        String location,
        List<TagSummary> foodTags,
        String friendshipStatus) {}
