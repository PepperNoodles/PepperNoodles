package com.peppernoodles.map.api;

import com.peppernoodles.map.api.dto.MapBoundsRequest;
import com.peppernoodles.map.api.dto.MapSearchRequest;
import com.peppernoodles.map.service.MapService;
import com.peppernoodles.restaurant.api.dto.RestaurantSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/map")
@Tag(name = "Map", description = "地圖搜尋：附近搜尋與視窗範圍查詢")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/nearby")
    @Operation(
            summary = "附近搜尋",
            description = "Restaurants within a radius of a point, nearest first, with real "
                    + "great-circle distances in metres.")
    public List<RestaurantSummary> nearby(@Valid MapSearchRequest request) {
        return mapService.nearby(request);
    }

    @GetMapping("/bounds")
    @Operation(summary = "地圖範圍查詢", description = "Markers inside the current viewport.")
    public List<RestaurantSummary> withinBounds(@Valid MapBoundsRequest request) {
        return mapService.withinBounds(request);
    }
}
