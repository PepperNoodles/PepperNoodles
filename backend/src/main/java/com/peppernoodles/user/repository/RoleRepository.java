package com.peppernoodles.user.repository;

import com.peppernoodles.user.domain.Role;
import com.peppernoodles.user.domain.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    default Role require(RoleName roleName) {
        return findByName(roleName.name())
                .orElseThrow(() -> new IllegalStateException(
                        "Role %s is missing; it should have been created by migration".formatted(roleName)));
    }
}
