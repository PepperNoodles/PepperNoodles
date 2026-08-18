package com.peppernoodles.support;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.restaurant.domain.BusinessHour;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import com.peppernoodles.shop.domain.Product;
import com.peppernoodles.shop.repository.ProductRepository;
import com.peppernoodles.tag.domain.FoodTag;
import com.peppernoodles.tag.repository.FoodTagRepository;
import com.peppernoodles.user.domain.RoleName;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.domain.UserProfile;
import com.peppernoodles.user.repository.RoleRepository;
import com.peppernoodles.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Builds the minimum persistent state a test needs.
 *
 * <p>Every builder makes its own unique user/address, so tests can run in any
 * order against the shared container without colliding on unique constraints.
 */
@Component
public class TestFixtures {

    public static final String PASSWORD = "Password123!";

    private static final AtomicLong COUNTER = new AtomicLong();

    private final UserRepository users;
    private final RoleRepository roles;
    private final RestaurantRepository restaurants;
    private final ProductRepository products;
    private final FoodTagRepository foodTags;
    private final PasswordEncoder passwordEncoder;

    public TestFixtures(
            UserRepository users,
            RoleRepository roles,
            RestaurantRepository restaurants,
            ProductRepository products,
            FoodTagRepository foodTags,
            PasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.restaurants = restaurants;
        this.products = products;
        this.foodTags = foodTags;
        this.passwordEncoder = passwordEncoder;
    }

    private static long next() {
        return COUNTER.incrementAndGet();
    }

    // --- users ---------------------------------------------------------------

    public User enabledUser(RoleName role) {
        long n = next();
        User user = new User("test%d@example.com".formatted(n), passwordEncoder.encode(PASSWORD));
        user.setEnabled(true);
        user.markEmailVerified();
        user.addRole(roles.require(role));

        UserProfile profile = new UserProfile();
        profile.setRealName("Test User");
        profile.setNickname("tester" + n);
        user.setProfile(profile);

        return users.save(user);
    }

    public User consumer() {
        return enabledUser(RoleName.ROLE_USER);
    }

    public User owner() {
        return enabledUser(RoleName.ROLE_COMPANY);
    }

    public User admin() {
        return enabledUser(RoleName.ROLE_ADMIN);
    }

    /** The principal the service layer sees for this user. */
    public AuthenticatedUser callerFor(User user) {
        Set<String> roleNames =
                user.getRoles().stream().map(r -> r.getName()).collect(java.util.stream.Collectors.toSet());
        return new AuthenticatedUser(user.getId(), user.getEmail(), roleNames);
    }

    // --- restaurants ---------------------------------------------------------

    public Restaurant restaurant(User owner) {
        long n = next();
        Restaurant restaurant = new Restaurant(
                owner,
                "測試餐廳" + n,
                "台北市測試區測試路" + n + "號",
                new BigDecimal("25.0330000"),
                new BigDecimal("121.5654000"));
        return restaurants.save(restaurant);
    }

    /** A restaurant at a specific point, for geo tests. */
    public Restaurant restaurantAt(User owner, String latitude, String longitude) {
        long n = next();
        return restaurants.save(new Restaurant(
                owner,
                "座標餐廳" + n,
                "台北市座標區座標路" + n + "號",
                new BigDecimal(latitude),
                new BigDecimal(longitude)));
    }

    public Restaurant restaurantOpenAllWeek(User owner, LocalTime opensAt, LocalTime closesAt) {
        Restaurant restaurant = restaurant(owner);
        for (short day = 0; day <= 6; day++) {
            restaurant.addBusinessHour(new BusinessHour(day, opensAt, closesAt));
        }
        return restaurants.save(restaurant);
    }

    // --- shop ----------------------------------------------------------------

    public Product listedProduct(Restaurant restaurant, String price, int quantity) {
        Product product = new Product();
        product.setRestaurant(restaurant);
        product.setName("測試商品" + next());
        product.setPrice(new BigDecimal(price));
        product.setQuantity(quantity);
        product.list();
        return products.save(product);
    }

    public Product delistedProduct(Restaurant restaurant) {
        Product product = new Product();
        product.setRestaurant(restaurant);
        product.setName("下架商品" + next());
        product.setPrice(new BigDecimal("100.00"));
        product.setQuantity(10);
        product.delist();
        return products.save(product);
    }

    // --- tags ----------------------------------------------------------------

    /** Reuses a seeded tag if one exists, otherwise creates one. */
    public FoodTag anyTag() {
        return foodTags.findAllByOrderByNameAsc().stream()
                .findFirst()
                .orElseGet(() -> foodTags.save(new FoodTag("測試標籤" + next())));
    }
}
