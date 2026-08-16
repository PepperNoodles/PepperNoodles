package com.peppernoodles.restaurant.repository;

import com.peppernoodles.restaurant.domain.Restaurant;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Reads the {@code restaurant_ratings} view.
 *
 * <p>The view is not mapped as an entity: Hibernate's schema validator and a
 * read-only view are an awkward pair, and nothing here ever writes. Callers
 * batch-load ratings for a page of restaurants with
 * {@link #findRatingsFor(Collection)} rather than querying per row.
 */
@Repository
public interface RestaurantRatingRepository extends JpaRepository<Restaurant, Long> {

    @Query(
            value =
                    """
                    select restaurant_id  as restaurantId,
                           rating_count   as ratingCount,
                           rating_average as ratingAverage,
                           review_count   as reviewCount
                      from restaurant_ratings
                     where restaurant_id = :restaurantId
                    """,
            nativeQuery = true)
    Optional<RatingRow> findRatingFor(@Param("restaurantId") Long restaurantId);

    @Query(
            value =
                    """
                    select restaurant_id  as restaurantId,
                           rating_count   as ratingCount,
                           rating_average as ratingAverage,
                           review_count   as reviewCount
                      from restaurant_ratings
                     where restaurant_id in (:restaurantIds)
                    """,
            nativeQuery = true)
    List<RatingRow> findRatingsFor(@Param("restaurantIds") Collection<Long> restaurantIds);

    interface RatingRow {
        Long getRestaurantId();

        Long getRatingCount();

        BigDecimal getRatingAverage();

        Long getReviewCount();
    }
}
