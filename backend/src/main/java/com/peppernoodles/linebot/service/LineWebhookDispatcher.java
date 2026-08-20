package com.peppernoodles.linebot.service;

// Boot 4 ships Jackson 3; the auto-configured bean is tools.jackson, not
// com.fasterxml.
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Turns a verified webhook payload into replies.
 *
 * <p>Reads the JSON with a tree model rather than binding to LINE's event
 * classes: the webhook carries a dozen event types this bot ignores, and an
 * unknown one must be skipped quietly, not fail deserialisation.
 */
@Component
public class LineWebhookDispatcher {

    private static final Logger log = LoggerFactory.getLogger(LineWebhookDispatcher.class);

    private final LineBotService lineBotService;
    private final LineMessagingClient messagingClient;
    private final ObjectMapper objectMapper;

    public LineWebhookDispatcher(
            LineBotService lineBotService, LineMessagingClient messagingClient, ObjectMapper objectMapper) {
        this.lineBotService = lineBotService;
        this.messagingClient = messagingClient;
        this.objectMapper = objectMapper;
    }

    /** @return how many events produced a reply */
    public int dispatch(String rawBody) {
        JsonNode root;
        try {
            root = objectMapper.readTree(rawBody);
        } catch (Exception e) {
            log.warn("Could not parse the LINE webhook payload: {}", e.getMessage());
            return 0;
        }

        JsonNode events = root.path("events");
        if (!events.isArray()) {
            return 0;
        }

        int replied = 0;
        for (JsonNode event : events) {
            String reply = replyFor(event);
            if (reply == null) {
                continue;
            }
            messagingClient.reply(event.path("replyToken").asText(null), reply);
            replied++;
        }
        return replied;
    }

    /** The bot's answer for one event, or null when it has nothing to say. */
    String replyFor(JsonNode event) {
        if (!"message".equals(event.path("type").asText())) {
            return null;
        }

        JsonNode message = event.path("message");
        return switch (message.path("type").asText()) {
            case "text" -> lineBotService.replyTo(message.path("text").asText(""));
            case "location" -> lineBotService.replyToLocation(
                    message.path("latitude").asDouble(), message.path("longitude").asDouble());
            default -> null;
        };
    }
}
