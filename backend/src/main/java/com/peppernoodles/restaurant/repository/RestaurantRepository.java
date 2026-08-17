package com.peppernoodles.restaurant.repository;

import com.peppernoodles.restaurant.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /**
     * Fetches only the tag set eagerly. Adding businessHours here produced a
     * cartesian product — seven opening rows times three tags came back as
     * twenty-one — because two collections cannot be joined in one query
     * without multiplying. The hours load separately inside the same
     * transaction.
     */
    @EntityGraph(attributePaths = "foodTags")
    Optional<Restaurant> findDetailedById(Long id);

    Page<Restaurant> findByOwnerId(Long ownerId, Pageable pageable);

    List<Restaurant> findByOwnerId(Long ownerId);

    boolean existsByAddress(String address);

    boolean existsByAddressAndIdNot(String address, Long id);

    /**
     * Name or address search. Backed by the trigram indexes on both columns.
     * Legacy equivalent used {@code like concat('%', :q, '%')} with no index.
     */
    @Query("""
            select distinct r from Restaurant r
             where lower(r.name)    like lower(concat('%', :q, '%'))
                or lower(r.address) like lower(concat('%', :q, '%'))
            """)
    Page<Restaurant> searchByNameOrAddress(@Param("q") String query, Pageable pageable);

    @Query("""
            select distinct r from Restaurant r
              join r.foodTags t
             where t.id in :tagIds
            """)
    Page<Restaurant> findByAnyTag(@Param("tagIds") List<Long> tagIds, Pageable pageable);
}
