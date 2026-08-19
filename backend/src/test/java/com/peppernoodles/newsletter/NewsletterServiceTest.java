package com.peppernoodles.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.newsletter.api.dto.NewsletterDtos.SubscribeRequest;
import com.peppernoodles.newsletter.domain.NewsletterSubscription;
import com.peppernoodles.newsletter.repository.NewsletterSubscriptionRepository;
import com.peppernoodles.newsletter.service.NewsletterService;
import com.peppernoodles.support.IntegrationTest;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 電子報訂閱 with double opt-in.
 *
 * <p>The raw tokens only ever exist in the mail, so these tests reach into the
 * repository for the hash and drive the service with a token they generate the
 * same way — the alternative would be parsing Mailpit from a unit test.
 */
class NewsletterServiceTest extends IntegrationTest {

    private static final AtomicLong COUNTER = new AtomicLong();

    @Autowired private NewsletterService newsletterService;
    @Autowired private NewsletterSubscriptionRepository subscriptions;

    private String uniqueEmail() {
        return "reader%d@example.com".formatted(COUNTER.incrementAndGet());
    }

    /** Drives confirm() with a token whose hash we install directly. */
    private String issueConfirmToken(NewsletterSubscription subscription) {
        String raw = "confirm-token-" + COUNTER.incrementAndGet();
        subscription.issueConfirmToken(
                com.peppernoodles.auth.service.SecureTokens.hash(raw), Instant.now().plusSeconds(3600));
        subscriptions.save(subscription);
        return raw;
    }

    @Test
    @DisplayName("subscribing stores the address lower-cased and unconfirmed")
    void subscribeStartsUnconfirmed() {
        String email = uniqueEmail();
        newsletterService.subscribe(new SubscribeRequest(email.toUpperCase(), "home", null));

        NewsletterSubscription saved = subscriptions.findByEmail(email).orElseThrow();
        assertThat(saved.getEmail()).isEqualTo(email);
        assertThat(saved.isConfirmed()).isFalse();
        // Nothing may be mailed to it until the reader confirms.
        assertThat(saved.isMailable()).isFalse();
        assertThat(saved.getSource()).isEqualTo("home");
    }

    @Test
    @DisplayName("subscribing twice reuses the row rather than creating a duplicate")
    void subscribeIsIdempotent() {
        String email = uniqueEmail();
        newsletterService.subscribe(new SubscribeRequest(email, "home", null));
        newsletterService.subscribe(new SubscribeRequest(email, "footer", null));

        assertThat(subscriptions.findAll().stream().filter(s -> s.getEmail().equals(email)))
                .hasSize(1);
    }

    @Test
    @DisplayName("confirming makes the address mailable")
    void confirmMakesMailable() {
        String email = uniqueEmail();
        newsletterService.subscribe(new SubscribeRequest(email, "home", null));
        String token = issueConfirmToken(subscriptions.findByEmail(email).orElseThrow());

        newsletterService.confirm(token);

        assertThat(subscriptions.findByEmail(email).orElseThrow().isMailable()).isTrue();
    }

    /** Otherwise a leaked link would keep working forever. */
    @Test
    @DisplayName("a confirmation token works only once")
    void confirmTokenIsSingleUse() {
        String email = uniqueEmail();
        newsletterService.subscribe(new SubscribeRequest(email, "home", null));
        String token = issueConfirmToken(subscriptions.findByEmail(email).orElseThrow());

        newsletterService.confirm(token);

        assertThatThrownBy(() -> newsletterService.confirm(token)).isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("an expired confirmation token is refused")
    void refusesExpiredToken() {
        String email = uniqueEmail();
        newsletterService.subscribe(new SubscribeRequest(email, "home", null));

        NewsletterSubscription subscription = subscriptions.findByEmail(email).orElseThrow();
        String raw = "expired-" + COUNTER.incrementAndGet();
        subscription.issueConfirmToken(
                com.peppernoodles.auth.service.SecureTokens.hash(raw), Instant.now().minusSeconds(60));
        subscriptions.save(subscription);

        assertThatThrownBy(() -> newsletterService.confirm(raw)).isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("an unknown confirmation token is refused")
    void refusesUnknownToken() {
        assertThatThrownBy(() -> newsletterService.confirm("not-a-real-token"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("unsubscribing needs only the token, and stops the address being mailable")
    void unsubscribeNeedsOnlyTheToken() {
        String email = uniqueEmail();
        newsletterService.subscribe(new SubscribeRequest(email, "home", null));
        newsletterService.confirm(issueConfirmToken(subscriptions.findByEmail(email).orElseThrow()));

        // confirm() rotates the unsubscribe token, so install a known one after it.
        NewsletterSubscription subscription = subscriptions.findByEmail(email).orElseThrow();
        String raw = "unsub-" + COUNTER.incrementAndGet();
        subscription.setUnsubscribeTokenHash(com.peppernoodles.auth.service.SecureTokens.hash(raw));
        subscriptions.save(subscription);

        newsletterService.unsubscribe(raw);

        NewsletterSubscription after = subscriptions.findByEmail(email).orElseThrow();
        assertThat(after.isMailable()).isFalse();
        assertThat(after.getUnsubscribedAt()).isNotNull();
    }

    @Test
    @DisplayName("an unknown unsubscribe token is refused")
    void refusesUnknownUnsubscribeToken() {
        assertThatThrownBy(() -> newsletterService.unsubscribe("nope"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("a blank address is rejected")
    void rejectsBlankEmail() {
        assertThatThrownBy(() -> newsletterService.subscribe(new SubscribeRequest("   ", "home", null)))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("stats count only addresses that may actually be mailed")
    void statsCountMailableOnly() {
        long before = newsletterService.stats().mailable();

        String pending = uniqueEmail();
        newsletterService.subscribe(new SubscribeRequest(pending, "home", null));
        assertThat(newsletterService.stats().mailable()).isEqualTo(before);

        String confirmed = uniqueEmail();
        newsletterService.subscribe(new SubscribeRequest(confirmed, "home", null));
        newsletterService.confirm(issueConfirmToken(subscriptions.findByEmail(confirmed).orElseThrow()));

        assertThat(newsletterService.stats().mailable()).isEqualTo(before + 1);
        assertThat(newsletterService.stats().total()).isGreaterThanOrEqualTo(before + 2);
    }
}
