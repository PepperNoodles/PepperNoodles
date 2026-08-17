package com.peppernoodles.restaurant.service;

import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.restaurant.api.dto.DiscoveryDtos.CampaignDto;
import com.peppernoodles.restaurant.api.dto.DiscoveryDtos.DistrictDto;
import com.peppernoodles.restaurant.api.dto.DiscoveryDtos.HighlightReviewDto;
import com.peppernoodles.restaurant.repository.DiscoveryRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Feeds 首頁 — 熱門地點 / 美食優惠 / 客戶評論. */
@Service
public class DiscoveryService {

    private static final ZoneId TAIPEI = ZoneId.of("Asia/Taipei");

    private final DiscoveryRepository discovery;
    private final StorageService storage;

    public DiscoveryService(DiscoveryRepository discovery, StorageService storage) {
        this.discovery = discovery;
        this.storage = storage;
    }

    @Transactional(readOnly = true)
    public List<DistrictDto> topDistricts(int limit) {
        return discovery.topDistricts(limit).stream()
                .map(row -> new DistrictDto(row.getDistrict(), row.getRestaurantCount()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CampaignDto> activeCampaigns(int limit) {
        return discovery.activeEvents(LocalDate.now(TAIPEI), limit).stream()
                .map(row -> new CampaignDto(
                        row.getId(),
                        row.getName(),
                        row.getContent(),
                        storage.publicUrl(StorageBucket.EVENT_PHOTOS, row.getImagePath()),
                        row.getStartsOn(),
                        row.getEndsOn(),
                        row.getRestaurantId(),
                        row.getRestaurantName()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HighlightReviewDto> highlightReviews(int limit) {
        return discovery.recentReviews(4, limit).stream()
                .map(row -> new HighlightReviewDto(
                        row.getId(),
                        row.getBody(),
                        row.getScore(),
                        row.getCreatedAt(),
                        row.getRestaurantId(),
                        row.getRestaurantName(),
                        row.getAuthorName(),
                        storage.publicUrl(StorageBucket.USER_AVATARS, row.getAuthorAvatarPath())))
                .toList();
    }
}
