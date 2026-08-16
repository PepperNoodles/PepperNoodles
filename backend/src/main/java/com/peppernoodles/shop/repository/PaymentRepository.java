package com.peppernoodles.shop.repository;

import com.peppernoodles.shop.domain.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @EntityGraph(attributePaths = {"order", "order.items"})
    Optional<Payment> findByMerchantTradeNo(String merchantTradeNo);
}
