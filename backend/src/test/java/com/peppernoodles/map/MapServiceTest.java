package com.peppernoodles.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peppernoodles.common.error.ApiExceptions.ValidationException;
import com.peppernoodles.map.api.dto.MapBoundsRequest;
import com.peppernoodles.map.api.dto.MapSearchRequest;
import com.peppernoodles.map.service.MapService;
import com.peppernoodles.restaurant.api.dto.RestaurantSummary;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 地圖搜尋 over the PostGIS geography column.
 *
 * <p>Reference points: Taipei Main Station (25.0478, 121.5170) and Taipei 101
 * (25.0330, 121.5654) are about 5.1 km apart.
 */
class MapServiceTest extends IntegrationTest {

    private static final double MAIN_STATION_LAT = 25.0478;
    private static final double MAIN_STATION_LNG = 121.5170;

    @Autowired private MapService mapService;
    @Autowired private TestFixtures fixtures;

    private static boolean containsId(List<RestaurantSummary> results, Long id) {
        return results.stream().anyMatch(r -> r.id().equals(id));
    }

    @Test
    @DisplayName("a nearby restaurant is found and a distant one is not")
    void findsOnlyWhatIsInRange() {
        User owner = fixtures.owner();
        // ~180 m north of Taipei Main Station.
        Restaurant near = fixtures.restaurantAt(owner, "25.0494", "121.5170");
        // Taipei 101, ~5.1 km away.
        Restaurant far = fixtures.restaurantAt(owner, "25.0330", "121.5654");

        List<RestaurantSummary> within500m = mapService.nearby(
                new MapSearchRequest(MAIN_STATION_LAT, MAIN_STATION_LNG, 500, 50));

        assertThat(containsId(within500m, near.getId())).isTrue();
        assertThat(containsId(within500m, far.getId())).isFalse();
    }

    @Test
    @DisplayName("widening the radius brings the distant restaurant in")
    void widerRadiusIncludesMore() {
        User owner = fixtures.owner();
        Restaurant far = fixtures.restaurantAt(owner, "25.0330", "121.5654");

        List<RestaurantSummary> within8km = mapService.nearby(
                new MapSearchRequest(MAIN_STATION_LAT, MAIN_STATION_LNG, 8000, 200));

        assertThat(containsId(within8km, far.getId())).isTrue();
    }

    @Test
    @DisplayName("results carry a real distance and are ordered nearest first")
    void reportsDistanceNearestFirst() {
        User owner = fixtures.owner();
        fixtures.restaurantAt(owner, "25.0494", "121.5170");
        fixtures.restaurantAt(owner, "25.0330", "121.5654");

        List<RestaurantSummary> results = mapService.nearby(
                new MapSearchRequest(MAIN_STATION_LAT, MAIN_STATION_LNG, 10_000, 200));

        assertThat(results).isNotEmpty();
        assertThat(results).allSatisfy(r -> assertThat(r.distanceMetres()).isNotNull());

        List<Double> distances = results.stream().map(RestaurantSummary::distanceMetres).toList();
        assertThat(distances).isSorted();
    }

    @Test
    @DisplayName("the viewport query returns what is inside the box")
    void findsWithinBounds() {
        User owner = fixtures.owner();
        Restaurant inside = fixtures.restaurantAt(owner, "25.0400", "121.5300");
        Restaurant outside = fixtures.restaurantAt(owner, "24.1477", "120.6736"); // Taichung

        List<RestaurantSummary> results =
                mapService.withinBounds(new MapBoundsRequest(25.03, 121.50, 25.06, 121.56, 200));

        assertThat(containsId(results, inside.getId())).isTrue();
        assertThat(containsId(results, outside.getId())).isFalse();
    }

    /**
     * Bean validation checks each corner on its own; only this cross-field rule
     * catches a box that has been turned inside out.
     */
    @Test
    @DisplayName("an inverted viewport is rejected rather than silently returning nothing")
    void rejectsInvertedBounds() {
        assertThatThrownBy(() ->
                        mapService.withinBounds(new MapBoundsRequest(25.06, 121.50, 25.03, 121.56, 200)))
                .isInstanceOf(ValidationException.class);

        assertThatThrownBy(() ->
                        mapService.withinBounds(new MapBoundsRequest(25.03, 121.56, 25.06, 121.50, 200)))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("the limit is honoured")
    void honoursLimit() {
        User owner = fixtures.owner();
        for (int i = 0; i < 4; i++) {
            fixtures.restaurantAt(owner, "25.04%02d".formatted(i), "121.5170");
        }

        assertThat(mapService.nearby(new MapSearchRequest(MAIN_STATION_LAT, MAIN_STATION_LNG, 20_000, 2)))
                .hasSizeLessThanOrEqualTo(2);
    }

    @Test
    @DisplayName("defaults apply when radius and limit are omitted")
    void appliesDefaults() {
        MapSearchRequest request = new MapSearchRequest(MAIN_STATION_LAT, MAIN_STATION_LNG, null, null);

        assertThat(request.radiusOrDefault()).isEqualTo(1_000);
        assertThat(request.limitOrDefault()).isEqualTo(50);
    }
}
