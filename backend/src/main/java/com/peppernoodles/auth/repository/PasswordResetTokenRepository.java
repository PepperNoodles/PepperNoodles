package com.peppernoodles.auth.repository;

import com.peppernoodles.auth.domain.PasswordResetToken;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    /** A new reset request invalidates any earlier one. */
    @Modifying
    @Query("""
            update PasswordResetToken t
               set t.consumedAt = :now
             where t.user.id = :userId
               and t.consumedAt is null
            """)
    int consumeAllForUser(@Param("userId") Long userId, @Param("now") Instant now);
}
