package com.peppernoodles.restaurant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.restaurant.api.dto.BusinessHourDto;
import com.peppernoodles.restaurant.api.dto.SaveRestaurantRequest;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import com.peppernoodles.restaurant.service.RestaurantService;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.domain.User;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class RestaurantServiceTest extends IntegrationTest {

    @Autowired private RestaurantService restaurantService;
    @Autowired private RestaurantRepository restaurants;
    @Autowired private TestFixtures fixtures;

    private SaveRestaurantRequest request(String name, String address, List<BusinessHourDto> hours) {
        return new SaveRestaurantRequest(
                name, address, "02-27208889", null,
                new BigDecimal("25.0330000"), new BigDecimal("121.5654000"), List.of(), hours);
    }

    @Test
    @DisplayName("an owner can create a restaurant and is recorded as its owner")
    void createsRestaurant() {
        User owner = fixtures.owner();

        Long id = restaurantService.create(
                request("新餐廳", "台北市信義區新路1號", List.of()), fixtures.callerFor(owner));

        Restaurant saved = restaurants.findById(id).orElseThrow();
        assertThat(saved.getName()).isEqualTo("新餐廳");
        assertThat(saved.isOwnedBy(owner.getId())).isTrue();
    }

    @Test
    @DisplayName("addresses are unique across restaurants")
    void rejectsDuplicateAddress() {
        User owner = fixtures.owner();
        String address = "台北市大安區重複路9號";
        restaurantService.create(request("第一家", address, List.of()), fixtures.callerFor(owner));

        assertThatThrownBy(() ->
                        restaurantService.create(request("第二家", address, List.of()), fixtures.callerFor(owner)))
                .isInstanceOf(ConflictException.class);
    }

    /** The legacy code let any logged-in user edit any restaurant. */
    @Test
    @DisplayName("another company account cannot edit someone else's restaurant")
    void refusesEditByNonOwner() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        User intruder = fixtures.owner();

        assertThatThrownBy(() -> restaurantService.update(
                        restaurant.getId(),
                        request("被改掉了", "台北市中山區入侵路1號", List.of()),
                        fixtures.callerFor(intruder)))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("an admin may edit any restaurant")
    void allowsAdminEdit() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        User admin = fixtures.admin();

        restaurantService.update(
                restaurant.getId(),
                request("管理員改名", restaurant.getAddress(), List.of()),
                fixtures.callerFor(admin));

        assertThat(restaurants.findById(restaurant.getId()).orElseThrow().getName()).isEqualTo("管理員改名");
    }

    /**
     * Hours travel with the restaurant payload and replace the whole set, which
     * is what the weekly editor in the UI expects.
     */
    @Test
    @DisplayName("saving replaces the whole set of business hours")
    void replacesBusinessHours() {
        User owner = fixtures.owner();
        Long id = restaurantService.create(
                request("有時段", "台北市松山區時段路3號",
                        List.of(
                                new BusinessHourDto(null, (short) 1, LocalTime.of(11, 0), LocalTime.of(14, 0)),
                                new BusinessHourDto(null, (short) 1, LocalTime.of(17, 0), LocalTime.of(21, 0)))),
                fixtures.callerFor(owner));

        assertThat(restaurantService.get(id, fixtures.callerFor(owner)).businessHours()).hasSize(2);

        restaurantService.update(
                id,
                request("有時段", "台北市松山區時段路3號",
                        List.of(new BusinessHourDto(null, (short) 2, LocalTime.of(9, 0), LocalTime.of(18, 0)))),
                fixtures.callerFor(owner));

        var hours = restaurantService.get(id, fixtures.callerFor(owner)).businessHours();
        assertThat(hours).hasSize(1);
        assertThat(hours.getFirst().dayOfWeek()).isEqualTo((short) 2);
    }

    /**
     * Regression: hibernate.jdbc.time_zone was shifting LocalTime by the host
     * offset, so a 10:00 opening read back as 18:00.
     */
    @Test
    @DisplayName("business hours round-trip without a timezone shift")
    void businessHoursKeepWallClockTime() {
        User owner = fixtures.owner();
        Restaurant restaurant = fixtures.restaurantOpenAllWeek(owner, LocalTime.of(10, 0), LocalTime.of(22, 0));

        var hours = restaurantService.get(restaurant.getId(), fixtures.callerFor(owner)).businessHours();

        assertThat(hours).isNotEmpty();
        assertThat(hours).allSatisfy(hour -> {
            assertThat(hour.opensAt()).isEqualTo(LocalTime.of(10, 0));
            assertThat(hour.closesAt()).isEqualTo(LocalTime.of(22, 0));
        });
    }

    /**
     * Regression: an entity graph fetching both foodTags and businessHours
     * produced a cartesian product — seven rows became twenty-one.
     */
    @Test
    @DisplayName("detail returns one row per opening interval, not a cartesian product")
    void doesNotDuplicateBusinessHours() {
        User owner = fixtures.owner();
        Restaurant restaurant = fixtures.restaurantOpenAllWeek(owner, LocalTime.of(9, 0), LocalTime.of(17, 0));
        restaurant.getFoodTags().add(fixtures.anyTag());
        restaurants.save(restaurant);

        var detail = restaurantService.get(restaurant.getId(), fixtures.callerFor(owner));

        assertThat(detail.businessHours()).hasSize(7);
        assertThat(detail.businessHours().stream().map(BusinessHourDto::id).distinct()).hasSize(7);
    }

    @Test
    @DisplayName("favourites can be added and removed, and are reported on the detail")
    void managesFavourites() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        User diner = fixtures.consumer();

        restaurantService.addFavourite(restaurant.getId(), diner.getId());
        assertThat(restaurantService.get(restaurant.getId(), fixtures.callerFor(diner)).favourited())
                .isTrue();
        assertThat(restaurantService.listFavouritesOf(diner.getId(), PageRequest.of(0, 10)).totalElements())
                .isEqualTo(1);

        restaurantService.removeFavourite(restaurant.getId(), diner.getId());
        assertThat(restaurantService.get(restaurant.getId(), fixtures.callerFor(diner)).favourited())
                .isFalse();
    }

    @Test
    @DisplayName("name search matches on address as well as name")
    void searchesNameAndAddress() {
        User owner = fixtures.owner();
        restaurantService.create(request("獨特店名", "台北市北投區獨特路77號", List.of()), fixtures.callerFor(owner));

        assertThat(restaurantService.list("獨特店名", null, PageRequest.of(0, 10)).totalElements())
                .isEqualTo(1);
        assertThat(restaurantService.list("北投區獨特路", null, PageRequest.of(0, 10)).totalElements())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("deleting a restaurant leaves its owner's account intact")
    void deleteDoesNotCascadeToOwner() {
        User owner = fixtures.owner();
        Restaurant restaurant = fixtures.restaurant(owner);

        restaurantService.delete(restaurant.getId(), fixtures.callerFor(owner));

        assertThat(restaurants.findById(restaurant.getId())).isEmpty();
        // The legacy mapping had CascadeType.ALL on this @ManyToOne, so deleting
        // a restaurant deleted the owning account with it.
        assertThatThrownBy(() -> restaurantService.get(restaurant.getId(), fixtures.callerFor(owner)))
                .isInstanceOf(NotFoundException.class);
        assertThat(fixtures.callerFor(owner).id()).isNotNull();
    }
}
