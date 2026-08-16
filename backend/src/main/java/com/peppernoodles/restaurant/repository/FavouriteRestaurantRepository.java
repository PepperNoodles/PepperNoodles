package com.peppernoodles.restaurant.repository;

import com.peppernoodles.restaurant.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 餐廳收藏. The join table carries no data beyond the pair and a timestamp, so it
 * is manipulated directly rather than mapped as an entity.
 */
@Repository
public interface FavouriteRestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = """
            select r.* from restaurants r
              join user_favourite_restaurants f on f.restaurant_id = r.id
             where f.user_id = :userId
             order by f.created_at desc
            """,
            countQuery = "select count(*) from user_favourite_restaurants where user_id = :userId",
            nativeQuery = true)
    Page<Restaurant> findFavouritesOf(@Param("userId") Long userId, Pageable pageable);

    @Modifying
    @Query(value = """
            insert into user_favourite_restaurants (user_id, restaurant_id)
            values (:userId, :restaurantId)
            on conflict do nothing
            """, nativeQuery = true)
    int addFavourite(@Param("userId") Long userId, @Param("restaurantId") Long restaurantId);

    @Modifying
    @Query(value = """
            delete from user_favourite_restaurants
             where user_id = :userId and restaurant_id = :restaurantId
            """, nativeQuery = true)
    int removeFavourite(@Param("userId") Long userId, @Param("restaurantId") Long restaurantId);

    @Query(value = """
            select exists(
              select 1 from user_favourite_restaurants
               where user_id = :userId and restaurant_id = :restaurantId)
            """, nativeQuery = true)
    boolean isFavourite(@Param("userId") Long userId, @Param("restaurantId") Long restaurantId);
}
