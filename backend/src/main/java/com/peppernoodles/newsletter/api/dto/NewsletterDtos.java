package com.peppernoodles.newsletter.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class NewsletterDtos {

    private NewsletterDtos() {}

    public record SubscribeRequest(
            @NotBlank @Email(message = "請輸入有效的電子信箱") @Size(max = 254) String email,
            String source,
            String recaptchaToken) {}

    public record TokenRequest(@NotBlank String token) {}

    /** Deliberately says nothing about whether the address was already known. */
    public record SubscribeResponse(String message) {}

    public record NewsletterStatsDto(long total, long mailable) {}
}
