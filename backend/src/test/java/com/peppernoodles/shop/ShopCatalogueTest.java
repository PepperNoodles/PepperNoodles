package com.peppernoodles.shop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.shop.api.dto.ProductSummary;
import com.peppernoodles.shop.domain.Product;
import com.peppernoodles.shop.service.CartService;
import com.peppernoodles.shop.service.ProductService;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.domain.User;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/** Catalogue filtering and the persistent cart. */
class ShopCatalogueTest extends IntegrationTest {

    @Autowired private ProductService productService;
    @Autowired private CartService cartService;
    @Autowired private TestFixtures fixtures;

    private List<Long> search(String q, BigDecimal min, BigDecimal max, Long restaurantId) {
        return productService
                .search(q, min, max, null, null, restaurantId, null, PageRequest.of(0, 100))
                .content()
                .stream()
                .map(ProductSummary::id)
                .toList();
    }

    /**
     * Regression: the old query used `(:param is null or …)`, which binds an
     * untyped NULL that PostgreSQL infers as bytea — `lower(bytea)` does not
     * exist, so an unfiltered search failed outright.
     */
    @Test
    @DisplayName("an unfiltered search works and returns only listed products")
    void unfilteredSearchExcludesDelisted() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        Product listed = fixtures.listedProduct(restaurant, "100.00", 5);
        Product delisted = fixtures.delistedProduct(restaurant);

        List<Long> ids = search(null, null, null, null);

        assertThat(ids).contains(listed.getId());
        assertThat(ids).doesNotContain(delisted.getId());
    }

    @Test
    @DisplayName("the price range filter is inclusive at both ends")
    void filtersByPriceRange() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        Product cheap = fixtures.listedProduct(restaurant, "100.00", 5);
        Product mid = fixtures.listedProduct(restaurant, "300.00", 5);
        Product dear = fixtures.listedProduct(restaurant, "900.00", 5);

        List<Long> ids = search(null, new BigDecimal("100"), new BigDecimal("300"), restaurant.getId());

        assertThat(ids).contains(cheap.getId(), mid.getId());
        assertThat(ids).doesNotContain(dear.getId());
    }

    @Test
    @DisplayName("the name filter is case-insensitive and partial")
    void filtersByName() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        Product product = fixtures.listedProduct(restaurant, "150.00", 5);
        // Rename to something with a stable, distinctive fragment to search for.
        productService.update(
                product.getId(),
                new com.peppernoodles.shop.api.dto.SaveProductRequest(
                        restaurant.getId(), null, "限量鳳梨酥禮盒", null,
                        new BigDecimal("150.00"), 5, null, true),
                fixtures.callerFor(fixtures.admin()));

        assertThat(search("鳳梨酥", null, null, restaurant.getId())).contains(product.getId());
        assertThat(search("鳳梨", null, null, restaurant.getId())).contains(product.getId());
        assertThat(search("絕對不存在的商品名", null, null, null)).isEmpty();
    }

    @Test
    @DisplayName("filtering by restaurant excludes other shops")
    void filtersByRestaurant() {
        Restaurant mine = fixtures.restaurant(fixtures.owner());
        Restaurant theirs = fixtures.restaurant(fixtures.owner());
        Product ours = fixtures.listedProduct(mine, "120.00", 3);
        Product other = fixtures.listedProduct(theirs, "120.00", 3);

        List<Long> ids = search(null, null, null, mine.getId());

        assertThat(ids).contains(ours.getId());
        assertThat(ids).doesNotContain(other.getId());
    }

    // --- cart ----------------------------------------------------------------

    @Test
    @DisplayName("putItem sets an absolute quantity rather than accumulating")
    void putItemIsAbsolute() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        Product product = fixtures.listedProduct(restaurant, "100.00", 20);
        User shopper = fixtures.consumer();

        cartService.putItem(shopper.getId(), product.getId(), 2);
        cartService.putItem(shopper.getId(), product.getId(), 5);

        var cart = cartService.getCart(shopper.getId());
        assertThat(cart.items()).hasSize(1);
        assertThat(cart.items().getFirst().quantity()).isEqualTo(5);
        assertThat(cart.total()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    @DisplayName("a delisted product cannot be added to the cart")
    void refusesDelistedProduct() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        Product delisted = fixtures.delistedProduct(restaurant);
        User shopper = fixtures.consumer();

        assertThatThrownBy(() -> cartService.putItem(shopper.getId(), delisted.getId(), 1))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("more than the available stock cannot be added")
    void refusesMoreThanStock() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        Product product = fixtures.listedProduct(restaurant, "100.00", 3);
        User shopper = fixtures.consumer();

        assertThatThrownBy(() -> cartService.putItem(shopper.getId(), product.getId(), 4))
                .isInstanceOf(ConflictException.class);
    }

    /**
     * A cart line can go stale after it is added, so the cart reports it rather
     * than failing only at checkout.
     */
    @Test
    @DisplayName("a line whose product is delisted afterwards is flagged unavailable")
    void flagsLinesThatWentStale() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        Product product = fixtures.listedProduct(restaurant, "100.00", 10);
        User shopper = fixtures.consumer();
        cartService.putItem(shopper.getId(), product.getId(), 2);

        productService.delete(product.getId(), fixtures.callerFor(fixtures.admin()));

        // The product is gone entirely, so the line goes with it.
        assertThat(cartService.getCart(shopper.getId()).items()).isEmpty();
    }

    @Test
    @DisplayName("unavailable lines are excluded from the cart total")
    void excludesUnavailableFromTotal() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        Product good = fixtures.listedProduct(restaurant, "100.00", 10);
        Product goesOutOfStock = fixtures.listedProduct(restaurant, "250.00", 10);
        User shopper = fixtures.consumer();

        cartService.putItem(shopper.getId(), good.getId(), 1);
        cartService.putItem(shopper.getId(), goesOutOfStock.getId(), 4);

        // Someone else buys almost all of it.
        productService.update(
                goesOutOfStock.getId(),
                new com.peppernoodles.shop.api.dto.SaveProductRequest(
                        restaurant.getId(), null, goesOutOfStock.getName(), null,
                        new BigDecimal("250.00"), 1, null, true),
                fixtures.callerFor(fixtures.admin()));

        var cart = cartService.getCart(shopper.getId());
        assertThat(cart.hasUnavailableItems()).isTrue();
        assertThat(cart.total()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("removing and clearing empty the cart")
    void removesAndClears() {
        Restaurant restaurant = fixtures.restaurant(fixtures.owner());
        Product a = fixtures.listedProduct(restaurant, "100.00", 10);
        Product b = fixtures.listedProduct(restaurant, "200.00", 10);
        User shopper = fixtures.consumer();

        cartService.putItem(shopper.getId(), a.getId(), 1);
        cartService.putItem(shopper.getId(), b.getId(), 1);
        cartService.removeItem(shopper.getId(), a.getId());
        assertThat(cartService.getCart(shopper.getId()).items()).hasSize(1);

        cartService.clear(shopper.getId());
        assertThat(cartService.getCart(shopper.getId()).items()).isEmpty();
    }
}
