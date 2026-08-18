package com.peppernoodles.map.repository;

import com.peppernoodles.restaurant.domain.Restaurant;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Geographic lookups, expressed in SQL because they use PostGIS.
 *
 * <p>The legacy "restaurants near me" fetched every row whose latitude and
 * longitude fell inside a hand-computed {@code BETWEEN} box — no index, no real
 * distance, and no ordering, so the nearest result could be anywhere in the
 * list. These queries use the generated {@code geo geography(Point,4326)}
 * column and its GiST index: {@code ST_DWithin} becomes an index lookup and
 * {@code ST_Distance} returns metres on the spheroid.
 *
 * <p>PostGIS functions are schema-qualified because the extension is installed
 * into {@code extensions} (see the first migration). Unqualified names resolve
 * on Supabase only because that schema happens to be on the role's search_path,
 * and fail on a plain Postgres container.
 */
@Repository
public interface RestaurantSearchRepository extends JpaRepository<Restaurant, Long> {

    /**
     * Restaurants within {@code radiusMetres} of ({@code lat}, {@code lng}),
     * nearest first, each with its rating rollup.
     */
    @Query(
            value =
                    """
                    select r.id                        as id,
                           r.name                      as name,
                           r.address                   as address,
                           r.contact                   as contact,
                           r.website                   as website,
                           r.photo_path                as photoPath,
                           r.latitude                  as latitude,
                           r.longitude                 as longitude,
                           extensions.st_distance(r.geo, o.g)     as distanceMetres,
                           rr.rating_count             as ratingCount,
                           rr.rating_average           as ratingAverage,
                           rr.review_count             as reviewCount
                      from restaurants r
                      cross join (
                        select extensions.st_setsrid(extensions.st_makepoint(:lng, :lat), 4326)::extensions.geography as g
                      ) o
                      left join restaurant_ratings rr on rr.restaurant_id = r.id
                     where extensions.st_dwithin(r.geo, o.g, :radiusMetres)
                     order by extensions.st_distance(r.geo, o.g)
                     limit :maxResults
                    """,
            nativeQuery = true)
    List<NearbyRestaurantRow> findNearby(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("radiusMetres") double radiusMetres,
            @Param("maxResults") int maxResults);

    /**
     * Every restaurant inside the map viewport, for drawing markers as the user
     * pans. Bounded by {@code maxResults} so a zoomed-out viewport cannot pull
     * the whole table into memory.
     */
    @Query(
            value =
                    """
                    select r.id                as id,
                           r.name              as name,
                           r.address           as address,
                           r.contact           as contact,
                           r.website           as website,
                           r.photo_path        as photoPath,
                           r.latitude          as latitude,
                           r.longitude         as longitude,
                           0.0                 as distanceMetres,
                           rr.rating_count     as ratingCount,
                           rr.rating_average   as ratingAverage,
                           rr.review_count     as reviewCount
                      from restaurants r
                      left join restaurant_ratings rr on rr.restaurant_id = r.id
                     where r.geo operator(extensions.&&) extensions.st_makeenvelope(:west, :south, :east, :north, 4326)::extensions.geography
                     limit :maxResults
                    """,
            nativeQuery = true)
    List<NearbyRestaurantRow> findWithinBounds(
            @Param("south") double south,
            @Param("west") double west,
            @Param("north") double north,
            @Param("east") double east,
            @Param("maxResults") int maxResults);

    /** Row shape shared by both queries. */
    interface NearbyRestaurantRow {
        Long getId();

        String getName();

        String getAddress();

        String getContact();

        String getWebsite();

        String getPhotoPath();

        BigDecimal getLatitude();

        BigDecimal getLongitude();

        Double getDistanceMetres();

        Long getRatingCount();

        BigDecimal getRatingAverage();

        Long getReviewCount();
    }
}
