package com.peppernoodles.linebot.service;

import com.peppernoodles.common.config.ApplicationProperties;
import com.peppernoodles.map.repository.RestaurantSearchRepository;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LINE bot restaurant lookup.
 *
 * <p>Replaces the legacy integration, which declared two conflicting
 * line-bot-spring-boot starters and required an ngrok tunnel on port 443 to
 * receive callbacks. Here the webhook is an ordinary endpoint whose
 * authenticity is checked from the {@code X-Line-Signature} header, and the
 * whole feature is disabled unless {@code peppernoodles.linebot.enabled} is set.
 */
@Service
public class LineBotService {

    private static final Logger log = LoggerFactory.getLogger(LineBotService.class);

    private final ApplicationProperties properties;
    private final RestaurantRepository restaurants;
    private final RestaurantSearchRepository search;

    public LineBotService(
            ApplicationProperties properties,
            RestaurantRepository restaurants,
            RestaurantSearchRepository search) {
        this.properties = properties;
        this.restaurants = restaurants;
        this.search = search;
    }

    public boolean isEnabled() {
        return properties.linebot().enabled();
    }

    /**
     * Verifies LINE's HMAC-SHA256 signature over the raw request body.
     *
     * <p>Without this anyone could POST to the webhook and drive the bot.
     */
    public boolean verifySignature(String rawBody, String signature) {
        if (signature == null || signature.isBlank()) {
            return false;
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    properties.linebot().channelSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            String expected = Base64.getEncoder()
                    .encodeToString(mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8)));
            return MessageDigest.isEqual(
                    expected.getBytes(StandardCharsets.UTF_8), signature.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Could not verify the LINE signature", e);
            return false;
        }
    }

    /** Produces the bot's reply for a plain-text message. */
    @Transactional(readOnly = true)
    public String replyTo(String userText) {
        String text = userText == null ? "" : userText.trim();

        if (text.isEmpty() || text.contains("你好") || text.contains("help") || text.contains("說明")) {
            return """
                   您好！我是胡椒MAP 🌶️
                   直接輸入地區或餐廳名稱就能查詢，例如：
                   ・台北市大安區
                   ・春水堂
                   """;
        }

        var matches = restaurants.searchByNameOrAddress(text, PageRequest.of(0, 5));
        if (matches.isEmpty()) {
            return "找不到符合「%s」的餐廳，換個關鍵字試試看？".formatted(text);
        }

        StringBuilder reply = new StringBuilder("為您找到 %d 間餐廳：\n".formatted(matches.getTotalElements()));
        matches.forEach(r -> reply.append("\n📍 %s\n   %s\n".formatted(r.getName(), r.getAddress())));
        return reply.toString();
    }

    /** Nearby search for a shared LINE location message. */
    @Transactional(readOnly = true)
    public String replyToLocation(double latitude, double longitude) {
        List<RestaurantSearchRepository.NearbyRestaurantRow> nearby =
                search.findNearby(latitude, longitude, 1000, 5);

        if (nearby.isEmpty()) {
            return "附近 1 公里內沒有登錄的餐廳 😢";
        }

        StringBuilder reply = new StringBuilder("附近的餐廳：\n");
        nearby.forEach(r -> reply.append(
                "\n📍 %s (%.0f 公尺)\n   %s\n".formatted(r.getName(), r.getDistanceMetres(), r.getAddress())));
        return reply.toString();
    }
}
