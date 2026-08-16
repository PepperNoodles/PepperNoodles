package com.peppernoodles.shop;

import static org.assertj.core.api.Assertions.assertThat;

import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import com.peppernoodles.shop.api.dto.CheckoutRequest;
import com.peppernoodles.shop.domain.Product;
import com.peppernoodles.shop.repository.CartItemRepository;
import com.peppernoodles.shop.repository.OrderRepository;
import com.peppernoodles.shop.repository.ProductRepository;
import com.peppernoodles.shop.service.CartService;
import com.peppernoodles.shop.service.OrderNumberGenerator;
import com.peppernoodles.shop.service.OrderService;
import com.peppernoodles.support.IntegrationTest;
import com.peppernoodles.support.TestFixtures;
import com.peppernoodles.user.domain.RoleName;
import com.peppernoodles.user.domain.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Regression tests for two races found by hand-testing concurrent checkouts.
 *
 * <p>Both bugs were silent: no exception, no log, just wrong data.
 */
class CheckoutConcurrencyTest extends IntegrationTest {

    @Autowired private OrderService orderService;
    @Autowired private CartService cartService;
    @Autowired private ProductRepository products;
    @Autowired private RestaurantRepository restaurants;
    @Autowired private CartItemRepository cartItems;
    @Autowired private OrderRepository orders;
    @Autowired private OrderNumberGenerator orderNumbers;
    @Autowired private TestFixtures fixtures;

    /**
     * Stock was oversold because {@code @Lock(PESSIMISTIC_WRITE)} returned the
     * Product already cached by the cart's entity graph, so every concurrent
     * checkout checked the same stale quantity.
     */
    @Test
    @DisplayName("concurrent checkouts never oversell")
    void concurrentCheckoutsDoNotOversell() throws Exception {
        int stock = 5;
        int buyers = 8;
        int unitsEach = 2;

        Product product = listedProduct(stock);
        List<User> shoppers = IntStream.range(0, buyers)
                .mapToObj(i -> fixtures.enabledUser(RoleName.ROLE_USER))
                .toList();
        shoppers.forEach(u -> cartService.putItem(u.getId(), product.getId(), unitsEach));

        List<Boolean> results = runConcurrently(shoppers.stream()
                .map(user -> (Callable<Boolean>) () -> {
                    try {
                        orderService.checkout(user.getId(), checkoutRequest());
                        return true;
                    } catch (RuntimeException expectedForLosers) {
                        return false;
                    }
                })
                .toList());

        long succeeded = results.stream().filter(Boolean::booleanValue).count();
        int remaining = products.findById(product.getId()).orElseThrow().getQuantity();

        // Only floor(5 / 2) = 2 buyers can be served.
        assertThat(succeeded).isEqualTo(2);
        assertThat(remaining).isEqualTo(stock - (2 * unitsEach)).isNotNegative();
    }

    /** Order numbers came from {@code count(*) + 1}, so simultaneous checkouts collided. */
    @Test
    @DisplayName("order numbers are unique under concurrency")
    void orderNumbersAreUnique() throws Exception {
        int callers = 32;

        List<String> generated = runConcurrently(
                IntStream.range(0, callers)
                        .mapToObj(i -> (Callable<String>) orderNumbers::next)
                        .toList());

        Set<String> distinct = ConcurrentHashMap.newKeySet();
        distinct.addAll(generated);
        assertThat(distinct).hasSize(callers);
        assertThat(generated).allMatch(no -> no.matches("PN\\d{8}-\\d{6}"));
    }

    @Test
    @DisplayName("checkout empties the cart and snapshots name and price")
    void checkoutSnapshotsProduct() {
        Product product = listedProduct(10);
        User shopper = fixtures.enabledUser(RoleName.ROLE_USER);
        cartService.putItem(shopper.getId(), product.getId(), 3);

        var order = orderService.checkout(shopper.getId(), checkoutRequest());

        assertThat(order.items()).hasSize(1);
        assertThat(order.items().getFirst().productName()).isEqualTo(product.getName());
        assertThat(order.items().getFirst().unitPrice()).isEqualByComparingTo(product.getPrice());
        assertThat(order.totalCost()).isEqualByComparingTo(product.getPrice().multiply(BigDecimal.valueOf(3)));
        assertThat(cartItems.findByUserId(shopper.getId())).isEmpty();
        assertThat(products.findById(product.getId()).orElseThrow().getQuantity()).isEqualTo(7);
    }

    @Test
    @DisplayName("cancelling an unpaid order returns its stock")
    void cancellingRestoresStock() {
        Product product = listedProduct(10);
        User shopper = fixtures.enabledUser(RoleName.ROLE_USER);
        cartService.putItem(shopper.getId(), product.getId(), 4);

        var order = orderService.checkout(shopper.getId(), checkoutRequest());
        assertThat(products.findById(product.getId()).orElseThrow().getQuantity()).isEqualTo(6);

        orderService.cancel(order.id(), adminCaller(shopper));

        assertThat(products.findById(product.getId()).orElseThrow().getQuantity()).isEqualTo(10);
        assertThat(orders.findById(order.id()).orElseThrow().getStatus().name()).isEqualTo("CANCELLED");
    }

    // --- helpers -------------------------------------------------------------

    private static com.peppernoodles.common.security.AuthenticatedUser adminCaller(User user) {
        return new com.peppernoodles.common.security.AuthenticatedUser(
                user.getId(), user.getEmail(), Set.of("ROLE_USER"));
    }

    private static CheckoutRequest checkoutRequest() {
        return new CheckoutRequest("收件人", "0912345678", "台北市信義區松高路11號");
    }

    private Product listedProduct(int quantity) {
        User owner = fixtures.enabledUser(RoleName.ROLE_COMPANY);
        Restaurant restaurant = restaurants.save(new Restaurant(
                owner,
                "測試餐廳" + System.nanoTime(),
                "台北市測試路" + System.nanoTime() + "號",
                new BigDecimal("25.0330000"),
                new BigDecimal("121.5654000")));

        Product product = new Product();
        product.setRestaurant(restaurant);
        product.setName("測試商品");
        product.setPrice(new BigDecimal("120.00"));
        product.setQuantity(quantity);
        product.list();
        return products.save(product);
    }

    /** Releases every task at the same instant so the window is as tight as possible. */
    private static <T> List<T> runConcurrently(List<Callable<T>> tasks) throws Exception {
        CyclicBarrier startLine = new CyclicBarrier(tasks.size());
        try (ExecutorService pool = Executors.newFixedThreadPool(tasks.size())) {
            List<Future<T>> futures = new ArrayList<>();
            for (Callable<T> task : tasks) {
                futures.add(pool.submit(() -> {
                    startLine.await();
                    return task.call();
                }));
            }
            List<T> results = new ArrayList<>();
            for (Future<T> future : futures) {
                results.add(future.get());
            }
            return results;
        }
    }
}
