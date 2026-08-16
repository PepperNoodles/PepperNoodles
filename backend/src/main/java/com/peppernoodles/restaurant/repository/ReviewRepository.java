package com.peppernoodles.restaurant.repository;

import com.peppernoodles.restaurant.domain.Review;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"author", "author.profile", "replies", "replies.author"})
    Page<Review> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "restaurant"})
    Optional<Review> findWithAuthorById(Long id);

    Page<Review> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);

    long countByRestaurantId(Long restaurantId);
}
