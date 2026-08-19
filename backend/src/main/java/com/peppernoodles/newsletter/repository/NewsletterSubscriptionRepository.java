package com.peppernoodles.newsletter.repository;

import com.peppernoodles.newsletter.domain.NewsletterSubscription;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterSubscriptionRepository extends JpaRepository<NewsletterSubscription, Long> {

    Optional<NewsletterSubscription> findByEmail(String email);

    Optional<NewsletterSubscription> findByConfirmTokenHash(String tokenHash);

    Optional<NewsletterSubscription> findByUnsubscribeTokenHash(String tokenHash);

    /** How many addresses could actually be mailed today. */
    @Query("""
            select count(s) from NewsletterSubscription s
             where s.confirmedAt is not null and s.unsubscribedAt is null
            """)
    long countMailable();
}
