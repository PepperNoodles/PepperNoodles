package com.peppernoodles.user.repository;

import com.peppernoodles.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Every {@code email} argument must already be normalised with
 * {@code EmailAddress.normalise} — addresses are stored lower-cased, so these
 * are exact-match lookups that use the plain unique index.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /** Login lookup. Roles are fetched here because the access token needs them. */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findWithRolesByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findWithRolesById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
