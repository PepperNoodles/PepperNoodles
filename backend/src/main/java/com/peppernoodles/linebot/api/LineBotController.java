package com.peppernoodles.linebot.api;

import com.peppernoodles.linebot.service.LineBotService;
import com.peppernoodles.linebot.service.LineWebhookDispatcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LINE Messaging API webhook.
 *
 * <p>Reachable without a JWT because LINE calls it; authenticity comes from the
 * {@code X-Line-Signature} HMAC over the raw body.
 */
@RestController
@RequestMapping("/api/v1/linebot")
@Tag(name = "LINE bot", description = "LINE 查詢餐廳")
public class LineBotController {

    private static final Logger log = LoggerFactory.getLogger(LineBotController.class);

    private final LineBotService lineBotService;
    private final LineWebhookDispatcher dispatcher;

    public LineBotController(LineBotService lineBotService, LineWebhookDispatcher dispatcher) {
        this.lineBotService = lineBotService;
        this.dispatcher = dispatcher;
    }

    @PostMapping("/webhook")
    @Operation(summary = "LINE webhook", description = "Verified by X-Line-Signature; disabled by default.")
    public ResponseEntity<Void> webhook(
            @RequestBody String rawBody,
            @RequestHeader(value = "X-Line-Signature", required = false) String signature) {

        if (!lineBotService.isEnabled()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        if (!lineBotService.verifySignature(rawBody, signature)) {
            log.warn("Rejected a LINE webhook call with an invalid signature");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Always 200: LINE retries anything else, and a reply token is dead
        // within about a minute, so a retry could not succeed anyway.
        int replied = dispatcher.dispatch(rawBody);
        log.info("Handled a LINE webhook payload ({} bytes, {} replies)", rawBody.length(), replied);
        return ResponseEntity.ok().build();
    }
}
