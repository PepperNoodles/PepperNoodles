package com.peppernoodles.shop.repository;

import com.peppernoodles.shop.domain.Product;
import com.peppernoodles.shop.domain.ProductStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph(attributePaths = {"restaurant", "subcategory", "foodTags"})
    Optional<Product> findDetailedById(Long id);

    /**
     * Atomically reserves {@code amount} units, returning the number of rows
     * changed: 1 on success, 0 when there was not enough stock.
     *
     * <p>This must be a single conditional UPDATE rather than a read, a check in
     * Java, and a write. An earlier version used {@code SELECT … FOR UPDATE} and
     * {@code Product#decreaseStock}, which oversold under concurrency: the
     * checkout had already loaded the Product through the cart's entity graph,
     * so the locking query returned the <em>cached</em> instance and the check
     * ran against a stale quantity. Three simultaneous orders for two units each
     * all passed a check against a stock of three. Doing the comparison and the
     * decrement in one statement leaves no window and no cache to go stale.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Product p
               set p.quantity = p.quantity - :amount
             where p.id = :id
               and p.quantity >= :amount
            """)
    int reserveStock(@Param("id") Long id, @Param("amount") int amount);

    /** Returns reserved units when an order is cancelled or expires. */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Product p set p.quantity = p.quantity + :amount where p.id = :id")
    int releaseStock(@Param("id") Long id, @Param("amount") int amount);

    /** Name, price and status without loading a managed entity that could go stale. */
    @Query("select p.id as id, p.name as name, p.price as price, p.status as status from Product p where p.id = :id")
    Optional<ProductSnapshot> findSnapshot(@Param("id") Long id);

    /** Immutable view of a product at checkout time. */
    interface ProductSnapshot {
        Long getId();

        String getName();

        java.math.BigDecimal getPrice();

        ProductStatus getStatus();
    }

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findByRestaurantId(Long restaurantId, Pageable pageable);

    /**
     * Products matching the member's interest tags — 根據使用者興趣推薦.
     * The legacy version hardcoded the e-mail "chris@gmail.com" in the controller.
     */
    @Query("""
            select distinct p from Product p
              join p.foodTags t
              join User u on u.id = :userId
              join u.foodTags ut on ut.id = t.id
             where p.status = com.peppernoodles.shop.domain.ProductStatus.LISTED
            """)
    Page<Product> recommendedFor(@Param("userId") Long userId, Pageable pageable);
}
