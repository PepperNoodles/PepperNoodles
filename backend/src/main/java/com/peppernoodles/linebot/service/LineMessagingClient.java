package com.peppernoodles.linebot.service;

import com.peppernoodles.common.config.ApplicationProperties;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Pushes replies back to LINE.
 *
 * <p>The 2021 project used the line-bot starter (two conflicting versions of it,
 * in fact). This talks to the Messaging API directly: one endpoint, one header.
 *
 * <p>A reply token is single-use and expires within about a minute, so a failure
 * is logged and dropped rather than retried — by the time a retry ran the token
 * would be dead anyway.
 */
@Component
public class LineMessagingClient {

    private static final Logger log = LoggerFactory.getLogger(LineMessagingClient.class);
    private static final String REPLY_ENDPOINT = "https://api.line.me/v2/bot/message/reply";

    /** LINE rejects a text message longer than this. */
    private static final int MAX_TEXT_LENGTH = 5000;

    private final ApplicationProperties properties;
    private final RestClient restClient;

    public LineMessagingClient(ApplicationProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.create();
    }

    /** True when a channel token is configured, so replies can actually be sent. */
    public boolean canReply() {
        String token = properties.linebot().channelToken();
        return properties.linebot().enabled() && token != null && !token.isBlank();
    }

    public void reply(String replyToken, String text) {
        if (!canReply()) {
            // Expected in development and CI: the feature is off and the
            // webhook has nothing to answer with.
            log.debug("Skipping LINE reply — no channel token configured");
            return;
        }
        if (replyToken == null || replyToken.isBlank() || text == null || text.isBlank()) {
            return;
        }

        Map<String, Object> payload = Map.of(
                "replyToken", replyToken,
                "messages", List.of(Map.of("type", "text", "text", truncate(text))));

        try {
            restClient
                    .post()
                    .uri(REPLY_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.linebot().channelToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Replied to a LINE message");
        } catch (RestClientException e) {
            // Never let a delivery failure turn into a 500 on the webhook —
            // LINE would then retry the whole event.
            log.error("Could not reply to LINE: {}", e.getMessage());
        }
    }

    static String truncate(String text) {
        return text.length() <= MAX_TEXT_LENGTH ? text : text.substring(0, MAX_TEXT_LENGTH - 1) + "…";
    }
}
