package com.peppernoodles.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 商品子分類. Legacy entity: {@code ProductDetailClass}. */
@Entity
@Table(name = "product_subcategories")
@Getter
@Setter
@NoArgsConstructor
public class ProductSubcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    @Column(nullable = false)
    private String name;
}
