package com.peppernoodles.restaurant.repository;

import com.peppernoodles.restaurant.domain.RestaurantEvent;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantEventRepository extends JpaRepository<RestaurantEvent, Long> {

    List<RestaurantEvent> findByRestaurantIdOrderByStartsOnDesc(Long restaurantId);

    @Query("""
            select e from RestaurantEvent e
             where e.restaurant.id = :restaurantId
               and e.startsOn <= :on and e.endsOn >= :on
             order by e.startsOn
            """)
    List<RestaurantEvent> findActive(@Param("restaurantId") Long restaurantId, @Param("on") LocalDate on);

    @Query("""
            select e from RestaurantEvent e
             where e.startsOn <= :on and e.endsOn >= :on
             order by e.startsOn desc
            """)
    Page<RestaurantEvent> findAllActive(@Param("on") LocalDate on, Pageable pageable);
}
