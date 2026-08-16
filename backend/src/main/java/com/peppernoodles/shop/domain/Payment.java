package com.peppernoodles.shop.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A payment attempt against an order.
 *
 * <p>New in the refactor. The legacy code called ECPay and recorded nothing
 * beyond flipping the order's status string, so a failed, duplicated, or
 * out-of-order callback left no trace to reconcile against.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String provider = "ECPAY";

    @Column(name = "merchant_trade_no", nullable = false, unique = true, updatable = false)
    private String merchantTradeNo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "raw_callback", columnDefinition = "jsonb")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    private String rawCallback;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    public Payment(Order order, String merchantTradeNo, BigDecimal amount) {
        this.order = order;
        this.merchantTradeNo = merchantTradeNo;
        this.amount = amount;
    }

    public enum Status {
        PENDING,
        SUCCEEDED,
        FAILED
    }
}
