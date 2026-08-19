package com.peppernoodles.newsletter.service;

import com.peppernoodles.auth.service.RecaptchaVerifier;
import com.peppernoodles.auth.service.SecureTokens;
import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.common.mail.MailService;
import com.peppernoodles.common.web.EmailAddress;
import com.peppernoodles.newsletter.api.dto.NewsletterDtos.NewsletterStatsDto;
import com.peppernoodles.newsletter.api.dto.NewsletterDtos.SubscribeRequest;
import com.peppernoodles.newsletter.domain.NewsletterSubscription;
import com.peppernoodles.newsletter.repository.NewsletterSubscriptionRepository;
import java.time.Duration;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 電子報訂閱 with double opt-in. */
@Service
public class NewsletterService {

    private static final Logger log = LoggerFactory.getLogger(NewsletterService.class);
    private static final Duration CONFIRM_WINDOW = Duration.ofDays(7);

    private final NewsletterSubscriptionRepository subscriptions;
    private final MailService mailService;
    private final RecaptchaVerifier recaptchaVerifier;

    public NewsletterService(
            NewsletterSubscriptionRepository subscriptions,
            MailService mailService,
            RecaptchaVerifier recaptchaVerifier) {
        this.subscriptions = subscriptions;
        this.mailService = mailService;
        this.recaptchaVerifier = recaptchaVerifier;
    }

    /**
     * Signs an address up, or re-sends the confirmation if it is already
     * pending.
     *
     * <p>Answers the same way whichever branch it takes: the form is public, so
     * telling a stranger whether an address is already subscribed would leak it.
     */
    @Transactional
    public void subscribe(SubscribeRequest request) {
        recaptchaVerifier.verify(request.recaptchaToken());

        String email = EmailAddress.normalise(request.email());
        if (email == null || email.isBlank()) {
            throw new ValidationException("請輸入有效的電子信箱。");
        }

        NewsletterSubscription subscription = subscriptions
                .findByEmail(email)
                .orElseGet(() -> subscriptions.save(new NewsletterSubscription(
                        email, SecureTokens.hash(SecureTokens.generate()), request.source())));

        if (subscription.isMailable()) {
            // Already subscribed and confirmed — nothing to do, and nothing to say.
            log.debug("Newsletter sign-up for an address that is already subscribed");
            return;
        }

        String rawToken = SecureTokens.generate();
        subscription.issueConfirmToken(SecureTokens.hash(rawToken), Instant.now().plus(CONFIRM_WINDOW));
        mailService.sendNewsletterConfirmation(email, rawToken);
    }

    /** Completes double opt-in. */
    @Transactional
    public void confirm(String rawToken) {
        NewsletterSubscription subscription = subscriptions
                .findByConfirmTokenHash(SecureTokens.hash(rawToken))
                .orElseThrow(() -> new ValidationException("確認連結無效或已經使用過了。"));

        if (subscription.confirmTokenExpired(Instant.now())) {
            throw new ValidationException("確認連結已過期，請重新訂閱。");
        }

        subscription.confirm();

        // The welcome mail carries the unsubscribe link, so the reader always
        // has a way out without needing an account.
        String rawUnsubscribe = SecureTokens.generate();
        subscription.setUnsubscribeTokenHash(SecureTokens.hash(rawUnsubscribe));
        mailService.sendNewsletterWelcome(subscription.getEmail(), rawUnsubscribe);

        log.info("Newsletter subscription confirmed");
    }

    /** One click, no login — anything else is a dark pattern. */
    @Transactional
    public void unsubscribe(String rawToken) {
        NewsletterSubscription subscription = subscriptions
                .findByUnsubscribeTokenHash(SecureTokens.hash(rawToken))
                .orElseThrow(() -> new ValidationException("取消訂閱連結無效。"));

        subscription.unsubscribe();
        log.info("Newsletter subscription cancelled");
    }

    @Transactional(readOnly = true)
    public NewsletterStatsDto stats() {
        return new NewsletterStatsDto(subscriptions.count(), subscriptions.countMailable());
    }
}
