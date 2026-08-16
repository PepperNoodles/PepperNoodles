package com.peppernoodles.shop.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A line on an order. Legacy entity: {@code OrderDetail}.
 *
 * <p>The name and unit price are <em>snapshots</em> taken at checkout. The legacy
 * row stored only a product FK and joined back for both, so editing or deleting
 * a product silently rewrote the history of every past order containing it.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /** Nullable: the product may later be deleted; the snapshot below survives. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_name", nullable = false, updatable = false)
    private String productName;

    @Column(name = "unit_price", nullable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    /**
     * @param product a reference to the product row; may be an unloaded proxy,
     *     because the name and price are passed in rather than read from it.
     *     Checkout deliberately avoids loading the entity — see
     *     {@code ProductRepository#reserveStock}.
     */
    public OrderItem(Product product, String productName, BigDecimal unitPrice, int quantity) {
        this.product = product;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
