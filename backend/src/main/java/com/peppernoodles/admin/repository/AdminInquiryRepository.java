package com.peppernoodles.admin.repository;

import com.peppernoodles.admin.domain.AdminInquiry;
import com.peppernoodles.admin.domain.AdminInquiry.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminInquiryRepository extends JpaRepository<AdminInquiry, Long> {

    @EntityGraph(attributePaths = {"user", "user.profile"})
    Page<AdminInquiry> findByStatusOrderByCreatedAtDesc(Status status, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "user.profile"})
    Page<AdminInquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);

    long countByStatus(Status status);
}
