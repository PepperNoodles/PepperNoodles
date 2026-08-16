package com.peppernoodles.shop.domain;

import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.user.domain.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 訂單. Legacy entity: {@code OrderList}. */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Human-readable unique order number, e.g. {@code PN20260817-000123}. The
     * legacy column was a bigint literally named {@code UUID}.
     */
    @Column(name = "order_no", nullable = false, unique = true, updatable = false)
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false)
    private String receiverPhone;

    @Column(name = "receiver_address", nullable = false)
    private String receiverAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "total_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    /** 訂單保留期限 — after this a PENDING order is swept to EXPIRED. */
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        item.setOrder(this);
        items.add(item);
    }

    public void recalculateTotal() {
        totalCost = items.stream()
                .map(OrderItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isOwnedBy(Long userId) {
        return user != null && user.getId().equals(userId);
    }

    public void markPaid() {
        if (status == OrderStatus.PAID) {
            return; // idempotent: payment gateways retry their callbacks
        }
        if (status != OrderStatus.PENDING) {
            throw new ConflictException("這筆訂單已無法付款（目前狀態：%s）。".formatted(status));
        }
        status = OrderStatus.PAID;
        paidAt = Instant.now();
    }

    public void cancel() {
        if (status != OrderStatus.PENDING) {
            throw new ConflictException("只有未付款的訂單可以取消。");
        }
        status = OrderStatus.CANCELLED;
        cancelledAt = Instant.now();
    }

    public void expire() {
        status = OrderStatus.EXPIRED;
        cancelledAt = Instant.now();
    }
}
