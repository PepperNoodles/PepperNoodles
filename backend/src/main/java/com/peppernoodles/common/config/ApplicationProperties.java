package com.peppernoodles.common.config;

import jakarta.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Every tunable the application owns, bound from the {@code peppernoodles.*}
 * namespace. Replaces the legacy habit of reading raw {@code @Value} strings —
 * and hardcoded credentials — from all over the codebase.
 */
@Validated
@ConfigurationProperties(prefix = "peppernoodles")
public record ApplicationProperties(
        Cors cors,
        Jwt jwt,
        Mail mail,
        Storage storage,
        Recaptcha recaptcha,
        Orders orders,
        Ecpay ecpay,
        LineBot linebot) {

    public record Cors(List<String> allowedOrigins) {}

    public record Jwt(
            @NotBlank String secret,
            @NotBlank String issuer,
            Duration accessTokenTtl,
            Duration refreshTokenTtl) {}

    public record Mail(
            String from,
            String frontendBaseUrl,
            Duration verificationTtl,
            Duration passwordResetTtl) {}

    /** Supabase Storage. The service key is backend-only and never leaves the server. */
    public record Storage(String url, String serviceKey) {

        public boolean isConfigured() {
            return serviceKey != null && !serviceKey.isBlank();
        }
    }

    public record Recaptcha(boolean enabled, String secret, String verifyUrl) {}

    public record Orders(Duration holdDuration, String expirySweepCron) {}

    public record Ecpay(
            boolean enabled,
            String merchantId,
            String hashKey,
            String hashIv,
            String mode,
            String returnUrl,
            String clientBackUrl) {}

    public record LineBot(boolean enabled, String channelToken, String channelSecret) {}
}
