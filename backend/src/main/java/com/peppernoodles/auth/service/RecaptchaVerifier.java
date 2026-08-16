package com.peppernoodles.auth.service;

import com.peppernoodles.common.config.ApplicationProperties;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

/**
 * Google reCAPTCHA verification for registration and the public contact form.
 *
 * <p>Disabled by default so local development and tests do not need Google
 * credentials; when {@code peppernoodles.recaptcha.enabled} is false the check
 * is skipped and a warning is logged once per call site.
 */
@Component
public class RecaptchaVerifier {

    private static final Logger log = LoggerFactory.getLogger(RecaptchaVerifier.class);

    private final ApplicationProperties properties;
    private final RestClient restClient;

    public RecaptchaVerifier(ApplicationProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.create();
    }

    /** @throws ValidationException when verification is enabled and the token is missing or rejected */
    public void verify(String token) {
        var config = properties.recaptcha();
        if (!config.enabled()) {
            log.debug("reCAPTCHA disabled; skipping verification");
            return;
        }
        if (token == null || token.isBlank()) {
            throw new ValidationException("請完成機器人驗證。");
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", config.secret());
        form.add("response", token);

        try {
            Map<?, ?> body = restClient
                    .post()
                    .uri(config.verifyUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(Map.class);

            if (body == null || !Boolean.TRUE.equals(body.get("success"))) {
                log.warn("reCAPTCHA rejected a submission: {}", body);
                throw new ValidationException("機器人驗證失敗，請重試。");
            }
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("reCAPTCHA verification call failed", e);
            throw new ValidationException("無法完成機器人驗證，請稍後再試。");
        }
    }
}
