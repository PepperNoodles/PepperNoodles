package com.peppernoodles.shop.repository;

import com.peppernoodles.shop.domain.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItem.Key> {

    @EntityGraph(attributePaths = {"product", "product.restaurant"})
    List<CartItem> findByUserId(Long userId);

    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    @Modifying
    @Query("delete from CartItem c where c.userId = :userId")
    int clearCart(@Param("userId") Long userId);

    @Modifying
    @Query("delete from CartItem c where c.userId = :userId and c.productId in :productIds")
    int removeAll(@Param("userId") Long userId, @Param("productIds") List<Long> productIds);
}
