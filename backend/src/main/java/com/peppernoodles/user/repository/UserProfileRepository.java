package com.peppernoodles.user.repository;

import com.peppernoodles.user.domain.UserProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(Long userId);

    /** Nickname search for the friend finder. Backed by a trigram index. */
    List<UserProfile> findTop20ByNicknameContainingIgnoreCase(String nickname);
}
