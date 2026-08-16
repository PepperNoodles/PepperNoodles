package com.peppernoodles.admin.repository;

import com.peppernoodles.admin.domain.AdminAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, Long> {

    @EntityGraph(attributePaths = "actor")
    Page<AdminAuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
