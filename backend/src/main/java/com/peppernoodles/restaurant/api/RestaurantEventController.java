package com.peppernoodles.restaurant.api;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.restaurant.api.dto.RestaurantEventDto;
import com.peppernoodles.restaurant.api.dto.SaveRestaurantEventRequest;
import com.peppernoodles.restaurant.service.RestaurantEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 餐廳活動 CRUD.
 *
 * <p>Reads are public; writes are restricted to the owning business account or
 * an admin, enforced in the service rather than by URL pattern.
 */
@RestController
@RequestMapping("/api/v1/restaurants")
@Tag(name = "Restaurant events", description = "餐廳活動")
public class RestaurantEventController {

    private final RestaurantEventService eventService;

    public RestaurantEventController(RestaurantEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{restaurantId}/events")
    @Operation(summary = "餐廳的所有活動", description = "Includes past and future events, newest first.")
    public List<RestaurantEventDto> list(@PathVariable Long restaurantId) {
        return eventService.listAll(restaurantId);
    }

    @PostMapping("/{restaurantId}/events")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "新增活動")
    public ResponseEntity<Void> create(
            @PathVariable Long restaurantId,
            @Valid @RequestBody SaveRestaurantEventRequest request,
            @CurrentUser AuthenticatedUser caller) {
        Long id = eventService.create(restaurantId, request, caller);
        return ResponseEntity.created(URI.create("/api/v1/restaurants/events/" + id)).build();
    }

    @PutMapping("/events/{eventId}")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "修改活動")
    public void update(
            @PathVariable Long eventId,
            @Valid @RequestBody SaveRestaurantEventRequest request,
            @CurrentUser AuthenticatedUser caller) {
        eventService.update(eventId, request, caller);
    }

    @DeleteMapping("/events/{eventId}")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除活動")
    public void delete(@PathVariable Long eventId, @CurrentUser AuthenticatedUser caller) {
        eventService.delete(eventId, caller);
    }

    @PostMapping(path = "/events/{eventId}/image", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "上傳活動圖片")
    public ImageUploaded uploadImage(
            @PathVariable Long eventId,
            @RequestPart("file") MultipartFile file,
            @CurrentUser AuthenticatedUser caller) {
        return new ImageUploaded(eventService.uploadImage(eventId, file, caller));
    }

    public record ImageUploaded(String imageUrl) {}
}
