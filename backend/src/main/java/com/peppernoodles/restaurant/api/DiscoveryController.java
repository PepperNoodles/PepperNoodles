package com.peppernoodles.restaurant.api;

import com.peppernoodles.restaurant.api.dto.DiscoveryDtos.CampaignDto;
import com.peppernoodles.restaurant.api.dto.DiscoveryDtos.DistrictDto;
import com.peppernoodles.restaurant.api.dto.DiscoveryDtos.HighlightReviewDto;
import com.peppernoodles.restaurant.service.DiscoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Public rollups used by 首頁. */
@RestController
@RequestMapping("/api/v1/discovery")
@Tag(name = "Discovery", description = "首頁：熱門地點、美食優惠、客戶評論")
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    public DiscoveryController(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @GetMapping("/districts")
    @Operation(summary = "熱門地點", description = "Restaurant counts per district, busiest first.")
    public List<DistrictDto> districts(@RequestParam(defaultValue = "8") int limit) {
        return discoveryService.topDistricts(limit);
    }

    @GetMapping("/campaigns")
    @Operation(summary = "美食優惠", description = "Restaurant campaigns running today.")
    public List<CampaignDto> campaigns(@RequestParam(defaultValue = "4") int limit) {
        return discoveryService.activeCampaigns(limit);
    }

    @GetMapping("/reviews")
    @Operation(summary = "客戶評論", description = "Recent reviews scored 4 or better.")
    public List<HighlightReviewDto> reviews(@RequestParam(defaultValue = "3") int limit) {
        return discoveryService.highlightReviews(limit);
    }
}
