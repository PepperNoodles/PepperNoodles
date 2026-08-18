package com.peppernoodles.restaurant;

import static org.assertj.core.api.Assertions.assertThat;

import com.peppernoodles.restaurant.api.dto.DiscoveryDtos.DistrictDto;
import com.peppernoodles.restaurant.api.dto.SaveRestaurantEventRequest;
import com.peppernoodles.restaurant.api.dto.SaveReviewRequest;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.service.DiscoveryService;
import com.peppernoodles.restaurant.service.RestaurantEventService;
import com.peppernoodles.restaurant.service.ReviewService;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.domain.User;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** The 首頁 rollups. */
class DiscoveryServiceTest extends IntegrationTest {

    @Autowired private DiscoveryService discoveryService;
    @Autowired private RestaurantEventService eventService;
    @Autowired private ReviewService reviewService;
    @Autowired private TestFixtures fixtures;

    /** The schema has no district column; it is parsed out of the address. */
    @Test
    @DisplayName("districts are counted from the address text")
    void countsDistrictsFromAddresses() {
        var districts = discoveryService.topDistricts(20);

        assertThat(districts).isNotEmpty();
        assertThat(districts).allSatisfy(d -> {
            assertThat(d.district()).endsWith("區");
            assertThat(d.restaurantCount()).isPositive();
        });
        // Busiest first.
        assertThat(districts.stream().map(DistrictDto::restaurantCount).toList()).isSortedAccordingTo((a, b) -> Long.compare(b, a));
    }

    @Test
    @DisplayName("only campaigns running today are returned")
    void returnsOnlyActiveCampaigns() {
        User owner = fixtures.owner();
        Restaurant restaurant = fixtures.restaurant(owner);
        LocalDate today = LocalDate.now();

        eventService.create(
                restaurant.getId(),
                new SaveRestaurantEventRequest("進行中活動", "現在有效", today.minusDays(1), today.plusDays(7)),
                fixtures.callerFor(owner));
        eventService.create(
                restaurant.getId(),
                new SaveRestaurantEventRequest("已結束活動", "早就過了", today.minusDays(30), today.minusDays(10)),
                fixtures.callerFor(owner));
        eventService.create(
                restaurant.getId(),
                new SaveRestaurantEventRequest("未開始活動", "還沒開始", today.plusDays(10), today.plusDays(20)),
                fixtures.callerFor(owner));

        var names = discoveryService.activeCampaigns(50).stream().map(c -> c.name()).toList();

        assertThat(names).contains("進行中活動");
        assertThat(names).doesNotContain("已結束活動", "未開始活動");
    }

    @Test
    @DisplayName("highlight reviews exclude anything scored below four")
    void excludesLowScoredReviews() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        reviewService.create(
                restaurant.getId(),
                new SaveReviewRequest("這則不該出現在首頁", (short) 2),
                fixtures.callerFor(fixtures.consumer()));
        reviewService.create(
                restaurant.getId(),
                new SaveReviewRequest("這則可以出現", (short) 5),
                fixtures.callerFor(fixtures.consumer()));

        var bodies = discoveryService.highlightReviews(100).stream().map(r -> r.body()).toList();

        assertThat(bodies).contains("這則可以出現");
        assertThat(bodies).doesNotContain("這則不該出現在首頁");
    }

    @Test
    @DisplayName("highlight reviews carry the restaurant they belong to")
    void carriesRestaurantContext() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        reviewService.create(
                restaurant.getId(), new SaveReviewRequest("很棒", (short) 5), fixtures.callerFor(fixtures.consumer()));

        var review = discoveryService.highlightReviews(100).stream()
                .filter(r -> r.restaurantId().equals(restaurant.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(review.restaurantName()).isEqualTo(restaurant.getName());
        assertThat(review.authorName()).isNotBlank();
    }
}
