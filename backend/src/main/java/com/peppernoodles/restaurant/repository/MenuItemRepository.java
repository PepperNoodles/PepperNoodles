package com.peppernoodles.restaurant.repository;

import com.peppernoodles.restaurant.domain.MenuItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantIdOrderByPositionAscIdAsc(Long restaurantId);
}
