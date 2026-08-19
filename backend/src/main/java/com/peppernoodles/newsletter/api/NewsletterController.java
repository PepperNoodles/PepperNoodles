package com.peppernoodles.newsletter.api;

import com.peppernoodles.newsletter.api.dto.NewsletterDtos.NewsletterStatsDto;
import com.peppernoodles.newsletter.api.dto.NewsletterDtos.SubscribeRequest;
import com.peppernoodles.newsletter.api.dto.NewsletterDtos.TokenRequest;
import com.peppernoodles.newsletter.service.NewsletterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** 電子報訂閱. All of the reader-facing endpoints are deliberately anonymous. */
@RestController
@RequestMapping("/api/v1/newsletter")
@Tag(name = "Newsletter", description = "電子報訂閱")
public class NewsletterController {

    private final NewsletterService newsletterService;

    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "訂閱電子報",
            description = "Always answers 202. A confirmation mail is sent unless the address is "
                    + "already subscribed; the response does not distinguish the two.")
    public void subscribe(@Valid @RequestBody SubscribeRequest request) {
        newsletterService.subscribe(request);
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "確認訂閱", description = "Single use; the link stops working afterwards.")
    public void confirm(@Valid @RequestBody TokenRequest request) {
        newsletterService.confirm(request.token());
    }

    @PostMapping("/unsubscribe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "取消訂閱", description = "No login required — the token is the authorisation.")
    public void unsubscribe(@Valid @RequestBody TokenRequest request) {
        newsletterService.unsubscribe(request.token());
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "訂閱統計")
    public NewsletterStatsDto stats() {
        return newsletterService.stats();
    }
}
