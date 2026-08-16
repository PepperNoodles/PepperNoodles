package com.peppernoodles.shop.domain;

import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.tag.domain.FoodTag;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A shop product. Legacy entity: {@code Product}.
 *
 * <p>Price is {@code numeric(10,2)}; the legacy column was an integer, so nothing
 * could be priced in cents.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private ProductSubcategory subcategory;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "image_path")
    private String imagePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.DELISTED;

    @Column(name = "released_at")
    private Instant releasedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_food_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<FoodTag> foodTags = new LinkedHashSet<>();

    public boolean isListed() {
        return status == ProductStatus.LISTED;
    }

    public void list() {
        this.status = ProductStatus.LISTED;
        if (releasedAt == null) {
            this.releasedAt = Instant.now();
        }
    }

    public void delist() {
        this.status = ProductStatus.DELISTED;
    }

    // Stock is changed only through ProductRepository#reserveStock and
    // #releaseStock, which do the check and the write in one atomic statement.
    // In-memory decrement helpers used to live here and oversold under
    // concurrency when the entity was stale.
}
