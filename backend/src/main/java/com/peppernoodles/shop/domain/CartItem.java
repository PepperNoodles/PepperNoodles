package com.peppernoodles.shop.domain;

import com.peppernoodles.user.domain.User;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 購物車項目.
 *
 * <p>New in the refactor: the legacy cart lived only in the HTTP session, so it
 * vanished on logout, could not be read by the order-expiry job, and could not
 * be resumed on another device.
 */
@Entity
@Table(name = "cart_items")
@IdClass(CartItem.Key.class)
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    public CartItem(Long userId, Long productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    /** Composite primary key (user_id, product_id). */
    public static class Key implements Serializable {
        private Long userId;
        private Long productId;

        public Key() {}

        public Key(Long userId, Long productId) {
            this.userId = userId;
            this.productId = productId;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Key k && Objects.equals(userId, k.userId) && Objects.equals(productId, k.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, productId);
        }
    }
}
