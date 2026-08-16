package com.peppernoodles.shop.repository;

import com.peppernoodles.shop.domain.Product;
import com.peppernoodles.shop.domain.ProductStatus;
import com.peppernoodles.tag.domain.FoodTag;
import jakarta.persistence.criteria.JoinType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

/**
 * Composable filters for the shop catalogue.
 *
 * <p>Two reasons this is a Specification rather than one big {@code @Query}:
 *
 * <ul>
 *   <li>The {@code (:param is null or column = :param)} idiom binds an untyped
 *       NULL, which PostgreSQL infers as {@code bytea} — so {@code lower(:q)}
 *       failed outright with <em>function lower(bytea) does not exist</em>.
 *   <li>Even where it works, that idiom stops the planner using an index,
 *       because the predicate is not sargable. Building only the predicates the
 *       caller actually asked for keeps each one indexable.
 * </ul>
 */
public final class ProductSpecifications {

    private ProductSpecifications() {}

    public static Specification<Product> hasStatus(ProductStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Product> nameContains(String term) {
        String pattern = "%" + term.toLowerCase(Locale.ROOT) + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern);
    }

    public static Specification<Product> priceAtLeast(BigDecimal min) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), min);
    }

    public static Specification<Product> priceAtMost(BigDecimal max) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), max);
    }

    public static Specification<Product> inCategory(Long categoryId) {
        return (root, query, cb) ->
                cb.equal(root.join("subcategory").join("category").get("id"), categoryId);
    }

    public static Specification<Product> inSubcategory(Long subcategoryId) {
        return (root, query, cb) -> cb.equal(root.join("subcategory").get("id"), subcategoryId);
    }

    public static Specification<Product> fromRestaurant(Long restaurantId) {
        return (root, query, cb) -> cb.equal(root.get("restaurant").get("id"), restaurantId);
    }

    public static Specification<Product> hasAnyTag(List<Long> tagIds) {
        return (root, query, cb) -> {
            // A product matching two of the requested tags would otherwise appear twice.
            if (query != null) {
                query.distinct(true);
            }
            var tags = root.<Product, FoodTag>join("foodTags", JoinType.INNER);
            return tags.get("id").in(tagIds);
        };
    }
}
