package com.peppernoodles.auth.repository;

import com.peppernoodles.auth.domain.EmailVerificationToken;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    /** Invalidates outstanding tokens when a fresh verification mail is requested. */
    @Modifying
    @Query("""
            update EmailVerificationToken t
               set t.consumedAt = :now
             where t.user.id = :userId
               and t.consumedAt is null
            """)
    int consumeAllForUser(@Param("userId") Long userId, @Param("now") Instant now);
}
