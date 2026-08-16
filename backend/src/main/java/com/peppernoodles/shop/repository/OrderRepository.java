package com.peppernoodles.shop.repository;

import com.peppernoodles.shop.domain.Order;
import com.peppernoodles.shop.domain.OrderStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"items", "user"})
    Optional<Order> findDetailedById(Long id);

    @EntityGraph(attributePaths = "items")
    Optional<Order> findByOrderNo(String orderNo);

    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);

    /** Feeds the 自動取消未付款訂單 sweep. */
    @EntityGraph(attributePaths = "items")
    List<Order> findByStatusAndExpiresAtBefore(OrderStatus status, Instant cutoff);
}
