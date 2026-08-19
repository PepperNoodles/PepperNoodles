package com.peppernoodles.admin.repository;

import com.peppernoodles.restaurant.domain.Restaurant;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The back-office view of restaurants: every row with its owner and rating,
 * which the public catalogue query does not expose.
 */
@Repository
public interface AdminRestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = """
            select r.id                as id,
                   r.name              as name,
                   r.address           as address,
                   r.contact           as contact,
                   u.id                as ownerUserId,
                   u.email             as ownerEmail,
                   coalesce(cp.real_name, p.nickname, u.email) as ownerName,
                   rr.review_count     as reviewCount,
                   rr.rating_average   as ratingAverage,
                   r.created_at        as createdAt
              from restaurants r
              join users u on u.id = r.owner_user_id
              left join company_profiles cp on cp.user_id = u.id
              left join user_profiles p     on p.user_id  = u.id
              left join restaurant_ratings rr on rr.restaurant_id = r.id
             where :q is null
                or r.name    ilike '%' || cast(:q as text) || '%'
                or r.address ilike '%' || cast(:q as text) || '%'
                or u.email   ilike '%' || cast(:q as text) || '%'
             order by r.created_at desc
            """,
            countQuery = """
            select count(*)
              from restaurants r
              join users u on u.id = r.owner_user_id
             where :q is null
                or r.name    ilike '%' || cast(:q as text) || '%'
                or r.address ilike '%' || cast(:q as text) || '%'
                or u.email   ilike '%' || cast(:q as text) || '%'
            """,
            nativeQuery = true)
    Page<ManagedRestaurantRow> search(@Param("q") String query, Pageable pageable);

    interface ManagedRestaurantRow {
        Long getId();

        String getName();

        String getAddress();

        String getContact();

        Long getOwnerUserId();

        String getOwnerEmail();

        String getOwnerName();

        Long getReviewCount();

        BigDecimal getRatingAverage();

        Instant getCreatedAt();
    }
}
