package com.peppernoodles.shop.repository;

import com.peppernoodles.shop.domain.ProductSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSubcategoryRepository extends JpaRepository<ProductSubcategory, Long> {}
