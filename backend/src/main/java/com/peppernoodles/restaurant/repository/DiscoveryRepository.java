package com.peppernoodles.restaurant.repository;

import com.peppernoodles.restaurant.domain.Restaurant;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Aggregate reads that feed the landing page.
 *
 * <p>Kept apart from {@link RestaurantRepository} because none of these return
 * restaurants — they are cross-cutting rollups (districts, live campaigns,
 * newest reviews) that exist only to populate 首頁.
 */
@Repository
public interface DiscoveryRepository extends JpaRepository<Restaurant, Long> {

    /**
     * 熱門地點 — restaurant counts per Taipei district, parsed out of the address.
     *
     * <p>The schema has no district column; addresses are free text. Extracting
     * it here keeps that assumption in one place rather than in the frontend.
     */
    @Query(value = """
            select district as district, count(*) as restaurantCount
              from (
                select substring(address from '[^市]*市([^區]+區)') as district
                  from restaurants
              ) t
             where district is not null
             group by district
             order by count(*) desc, district
             limit :limit
            """, nativeQuery = true)
    List<DistrictRow> topDistricts(@Param("limit") int limit);

    /** 美食優惠 — campaigns running today, newest first. */
    @Query(value = """
            select e.id            as id,
                   e.name          as name,
                   e.content       as content,
                   e.image_path    as imagePath,
                   e.starts_on     as startsOn,
                   e.ends_on       as endsOn,
                   r.id            as restaurantId,
                   r.name          as restaurantName
              from restaurant_events e
              join restaurants r on r.id = e.restaurant_id
             where :today between e.starts_on and e.ends_on
             order by e.starts_on desc
             limit :limit
            """, nativeQuery = true)
    List<EventRow> activeEvents(@Param("today") LocalDate today, @Param("limit") int limit);

    /** 客戶評論 — recent, well-scored reviews to show on the landing page. */
    @Query(value = """
            select rv.id         as id,
                   rv.body       as body,
                   rv.score      as score,
                   rv.created_at as createdAt,
                   r.id          as restaurantId,
                   r.name        as restaurantName,
                   coalesce(p.nickname, '會員') as authorName,
                   p.avatar_path as authorAvatarPath
              from restaurant_reviews rv
              join restaurants r on r.id = rv.restaurant_id
              left join user_profiles p on p.user_id = rv.author_user_id
             where rv.score >= :minScore
             order by rv.created_at desc
             limit :limit
            """, nativeQuery = true)
    List<ReviewRow> recentReviews(@Param("minScore") int minScore, @Param("limit") int limit);

    interface DistrictRow {
        String getDistrict();

        Long getRestaurantCount();
    }

    interface EventRow {
        Long getId();

        String getName();

        String getContent();

        String getImagePath();

        LocalDate getStartsOn();

        LocalDate getEndsOn();

        Long getRestaurantId();

        String getRestaurantName();
    }

    interface ReviewRow {
        Long getId();

        String getBody();

        Integer getScore();

        Instant getCreatedAt();

        Long getRestaurantId();

        String getRestaurantName();

        String getAuthorName();

        String getAuthorAvatarPath();
    }
}
