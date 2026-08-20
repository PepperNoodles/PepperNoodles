package com.peppernoodles.linebot.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The LINE bot's own logic.
 *
 * <p>Nothing here talks to LINE: the channel for this project was deleted, so
 * the reply push is exercised only as far as "would it try, and with what".
 * Everything up to that point — signature verification, event dispatch, and the
 * text the bot would send — is real.
 */
class LineBotTest extends IntegrationTest {

    @Autowired private LineBotService lineBotService;
    @Autowired private LineWebhookDispatcher dispatcher;
    @Autowired private LineMessagingClient messagingClient;
    @Autowired private TestFixtures fixtures;

    // --- signature -----------------------------------------------------------

    @Test
    @DisplayName("a request with no signature is rejected")
    void rejectsMissingSignature() {
        assertThat(lineBotService.verifySignature("{}", null)).isFalse();
        assertThat(lineBotService.verifySignature("{}", "")).isFalse();
    }

    /** Without this anyone could POST to the webhook and drive the bot. */
    @Test
    @DisplayName("a forged signature is rejected")
    void rejectsForgedSignature() {
        assertThat(lineBotService.verifySignature("{\"events\":[]}", "not-a-real-signature")).isFalse();
    }

    // --- replies -------------------------------------------------------------

    @Test
    @DisplayName("a greeting gets the help text")
    void greetingGetsHelp() {
        assertThat(lineBotService.replyTo("你好")).contains("胡椒MAP");
        assertThat(lineBotService.replyTo("")).contains("胡椒MAP");
    }

    @Test
    @DisplayName("a name that matches finds the restaurant")
    void findsRestaurantByName() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());

        String reply = lineBotService.replyTo(restaurant.getName());

        assertThat(reply).contains(restaurant.getName());
        assertThat(reply).contains(restaurant.getAddress());
    }

    @Test
    @DisplayName("a name that matches nothing says so rather than returning an empty list")
    void handlesNoMatches() {
        assertThat(lineBotService.replyTo("絕對不存在的店名ZZZ")).contains("找不到");
    }

    @Test
    @DisplayName("a shared location lists what is nearby with distances")
    void repliesToLocation() {
        // Taipei Main Station, with a restaurant ~180 m north.
        fixtures.restaurantAt(fixtures.owner(), "25.0494", "121.5170");

        String reply = lineBotService.replyToLocation(25.0478, 121.5170);

        assertThat(reply).contains("公尺");
    }

    @Test
    @DisplayName("a location with nothing around it says so")
    void handlesEmptyNeighbourhood() {
        // Middle of the Pacific.
        assertThat(lineBotService.replyToLocation(0.0, -160.0)).contains("附近");
    }

    // --- dispatch ------------------------------------------------------------

    @Test
    @DisplayName("a text event produces a reply")
    void dispatchesTextEvents() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        String payload =
                """
                {"events":[{"type":"message","replyToken":"tok","message":{"type":"text","text":"%s"}}]}
                """
                        .formatted(restaurant.getName());

        // Replying is a no-op without a channel token, but the event still counts
        // as handled — that is the branch we can verify without LINE.
        assertThat(dispatcher.dispatch(payload)).isEqualTo(1);
    }

    @Test
    @DisplayName("event types the bot does not handle are skipped quietly")
    void ignoresOtherEventTypes() {
        assertThat(dispatcher.dispatch("""
                {"events":[{"type":"follow","replyToken":"tok"},
                           {"type":"message","replyToken":"t2","message":{"type":"sticker"}}]}
                """))
                .isZero();
    }

    @Test
    @DisplayName("a malformed payload is dropped rather than throwing")
    void survivesMalformedPayload() {
        assertThat(dispatcher.dispatch("not json at all")).isZero();
        assertThat(dispatcher.dispatch("{}")).isZero();
        assertThat(dispatcher.dispatch("""
                {"events":"not-an-array"}
                """))
                .isZero();
    }

    @Test
    @DisplayName("replying is disabled while no channel token is configured")
    void replyDisabledWithoutToken() {
        // The project's channel was deleted; this is the state the app ships in.
        assertThat(messagingClient.canReply()).isFalse();
        // And calling it anyway must not throw.
        messagingClient.reply("tok", "hello");
    }

    @Test
    @DisplayName("an over-long reply is truncated to LINE's limit")
    void truncatesLongText() {
        String truncated = LineMessagingClient.truncate("字".repeat(6000));

        assertThat(truncated).hasSize(5000);
        assertThat(truncated).endsWith("…");
    }
}
