package com.peppernoodles.restaurant.repository;

import com.peppernoodles.restaurant.domain.ReviewReply;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {

    @EntityGraph(attributePaths = {"author", "review", "review.restaurant"})
    Optional<ReviewReply> findWithContextById(Long id);
}
