package com.peppernoodles.shop.repository;

import com.peppernoodles.shop.domain.ProductCategory;
import com.peppernoodles.shop.domain.ProductSubcategory;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @EntityGraph(attributePaths = "subcategories")
    List<ProductCategory> findAllByOrderByNameAsc();
}
