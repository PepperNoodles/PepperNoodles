package com.peppernoodles.restaurant.domain;

import com.peppernoodles.tag.domain.FoodTag;
import com.peppernoodles.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A restaurant. Legacy entity: {@code Restaurant}.
 *
 * <p>Two things the legacy mapping got dangerously wrong and this one does not:
 *
 * <ul>
 *   <li>The owner association had {@code CascadeType.ALL} on a {@code @ManyToOne},
 *       so removing a restaurant removed the owning account with it. Here the
 *       database uses {@code ON DELETE RESTRICT} and JPA cascades nothing upward.
 *   <li>{@code totalScore} and a <em>string</em> {@code restaurantAmount} were
 *       maintained by hand from several controllers. Ratings now come from the
 *       {@code restaurant_ratings} view.
 * </ul>
 *
 * <p>The {@code geo} column is {@code GENERATED ALWAYS} in Postgres and is
 * deliberately not mapped; radius search runs as a native query.
 */
@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_user_id", nullable = false, updatable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String address;

    @Column
    private String contact;

    @Column
    private String website;

    /** Object path in the {@code restaurant-photos} bucket. Legacy: a BLOB column. */
    @Column(name = "photo_path")
    private String photoPath;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "restaurant_food_tags",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<FoodTag> foodTags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("dayOfWeek asc, opensAt asc")
    private List<BusinessHour> businessHours = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("position asc, id asc")
    private List<MenuItem> menuItems = new ArrayList<>();

    public Restaurant(User owner, String name, String address, BigDecimal latitude, BigDecimal longitude) {
        this.owner = owner;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isOwnedBy(Long userId) {
        return owner != null && owner.getId().equals(userId);
    }

    public void replaceBusinessHours(List<BusinessHour> hours) {
        businessHours.clear();
        hours.forEach(this::addBusinessHour);
    }

    public void addBusinessHour(BusinessHour hour) {
        hour.setRestaurant(this);
        businessHours.add(hour);
    }

    public void addMenuItem(MenuItem item) {
        item.setRestaurant(this);
        menuItems.add(item);
    }
}
