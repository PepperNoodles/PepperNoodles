package com.peppernoodles.shop.service;

import com.peppernoodles.common.config.ApplicationProperties;
import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.shop.api.dto.CheckoutRequest;
import com.peppernoodles.shop.api.dto.OrderDto;
import com.peppernoodles.shop.domain.CartItem;
import com.peppernoodles.shop.domain.Order;
import com.peppernoodles.shop.domain.OrderItem;
import com.peppernoodles.shop.domain.OrderStatus;
import com.peppernoodles.shop.domain.Product;
import com.peppernoodles.shop.repository.CartItemRepository;
import com.peppernoodles.shop.repository.OrderRepository;
import com.peppernoodles.shop.domain.ProductStatus;
import com.peppernoodles.shop.repository.ProductRepository;
import com.peppernoodles.shop.repository.ProductRepository.ProductSnapshot;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 訂單 — checkout, cancellation, and the unpaid-order sweep. */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orders;
    private final ProductRepository products;
    private final CartItemRepository cartItems;
    private final UserRepository users;
    private final OrderNumberGenerator orderNumbers;
    private final ApplicationProperties properties;
    private final ShopMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    public OrderService(
            OrderRepository orders,
            ProductRepository products,
            CartItemRepository cartItems,
            UserRepository users,
            OrderNumberGenerator orderNumbers,
            ApplicationProperties properties,
            ShopMapper mapper) {
        this.orders = orders;
        this.products = products;
        this.cartItems = cartItems;
        this.users = users;
        this.orderNumbers = orderNumbers;
        this.properties = properties;
        this.mapper = mapper;
    }

    /**
     * 結帳 — converts the caller's cart into a PENDING order and reserves stock.
     *
     * <p>Stock rows are locked with {@code SELECT … FOR UPDATE} and always in
     * ascending product-id order. The lock closes the oversell race the legacy
     * checkout had (read stock, check, write, with nothing in between); the
     * consistent ordering prevents two concurrent checkouts holding one row each
     * and deadlocking on the other.
     */
    @Transactional
    public OrderDto checkout(Long userId, CheckoutRequest request) {
        List<CartItem> cart = cartItems.findByUserId(userId);
        if (cart.isEmpty()) {
            throw new ConflictException("購物車是空的。");
        }

        User user = users.findById(userId).orElseThrow(() -> NotFoundException.of("使用者", userId));

        Order order = new Order();
        order.setOrderNo(orderNumbers.next());
        order.setUser(user);
        order.setReceiverName(request.receiverName());
        order.setReceiverPhone(request.receiverPhone());
        order.setReceiverAddress(request.receiverAddress());
        order.setExpiresAt(Instant.now().plus(properties.orders().holdDuration()));

        // Reserve in ascending product order so two concurrent checkouts touching
        // the same pair of products cannot deadlock against each other.
        List<CartItem> ordered = cart.stream()
                .sorted(Comparator.comparing(CartItem::getProductId))
                .toList();

        for (CartItem item : ordered) {
            Long productId = item.getProductId();
            ProductSnapshot snapshot = products
                    .findSnapshot(productId)
                    .orElseThrow(() -> NotFoundException.of("商品", productId));

            if (snapshot.getStatus() != ProductStatus.LISTED) {
                throw new ConflictException("「%s」目前未上架，請從購物車移除。".formatted(snapshot.getName()));
            }

            // Atomic: 0 rows means another checkout took the stock first.
            if (products.reserveStock(productId, item.getQuantity()) == 0) {
                throw new ConflictException("「%s」庫存不足，請調整數量後再結帳。".formatted(snapshot.getName()));
            }

            order.addItem(new OrderItem(
                    entityManager.getReference(Product.class, productId),
                    snapshot.getName(),
                    snapshot.getPrice(),
                    item.getQuantity()));
        }

        order.recalculateTotal();
        orders.save(order);
        cartItems.clearCart(userId);

        log.info("User {} checked out order {} for {}", userId, order.getOrderNo(), order.getTotalCost());
        return mapper.toOrder(order);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderDto> listMine(Long userId, Pageable pageable) {
        return PageResponse.of(orders.findByUserIdOrderByCreatedAtDesc(userId, pageable), mapper::toOrder);
    }

    @Transactional(readOnly = true)
    public OrderDto get(Long orderId, AuthenticatedUser caller) {
        Order order = orders.findDetailedById(orderId).orElseThrow(() -> NotFoundException.of("訂單", orderId));
        if (!order.isOwnedBy(caller.id()) && !caller.isAdmin()) {
            throw new ForbiddenException("您沒有權限查看這筆訂單。");
        }
        return mapper.toOrder(order);
    }

    @Transactional
    public void cancel(Long orderId, AuthenticatedUser caller) {
        Order order = orders.findDetailedById(orderId).orElseThrow(() -> NotFoundException.of("訂單", orderId));
        if (!order.isOwnedBy(caller.id()) && !caller.isAdmin()) {
            throw new ForbiddenException("您沒有權限取消這筆訂單。");
        }

        order.cancel();
        releaseStock(order);
        log.info("Order {} cancelled by user {}", order.getOrderNo(), caller.id());
    }

    /** Called by the scheduled sweep and by the payment callback on failure. */
    @Transactional
    public int expireOverdueOrders() {
        List<Order> overdue = orders.findByStatusAndExpiresAtBefore(OrderStatus.PENDING, Instant.now());
        for (Order order : overdue) {
            order.expire();
            releaseStock(order);
        }
        if (!overdue.isEmpty()) {
            log.info("Expired {} unpaid orders", overdue.size());
        }
        return overdue.size();
    }

    /** Returns reserved stock when an order does not complete. */
    private void releaseStock(Order order) {
        order.getItems().stream()
                .filter(item -> item.getProduct() != null)
                .forEach(item -> products.releaseStock(item.getProduct().getId(), item.getQuantity()));
    }
}
