package com.peppernoodles.shop.service;

import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.restaurant.domain.Restaurant;
import com.peppernoodles.restaurant.repository.RestaurantRepository;
import com.peppernoodles.shop.api.dto.ProductDetail;
import com.peppernoodles.shop.api.dto.ProductSummary;
import com.peppernoodles.shop.api.dto.SaveProductRequest;
import com.peppernoodles.shop.domain.Product;
import com.peppernoodles.shop.domain.ProductStatus;
import com.peppernoodles.shop.repository.ProductRepository;
import com.peppernoodles.shop.repository.ProductSpecifications;
import com.peppernoodles.shop.repository.ProductSubcategoryRepository;
import com.peppernoodles.tag.repository.FoodTagRepository;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/** 商城商品 — catalogue browsing and owner-side product management. */
@Service
public class ProductService {

    private final ProductRepository products;
    private final ProductSubcategoryRepository subcategories;
    private final RestaurantRepository restaurants;
    private final FoodTagRepository foodTags;
    private final StorageService storage;
    private final ShopMapper mapper;

    public ProductService(
            ProductRepository products,
            ProductSubcategoryRepository subcategories,
            RestaurantRepository restaurants,
            FoodTagRepository foodTags,
            StorageService storage,
            ShopMapper mapper) {
        this.products = products;
        this.subcategories = subcategories;
        this.restaurants = restaurants;
        this.foodTags = foodTags;
        this.storage = storage;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductSummary> search(
            String query,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Long subcategoryId,
            Long restaurantId,
            List<Long> tagIds,
            Pageable pageable) {

        // Only the filters the caller actually supplied become predicates.
        Specification<Product> spec = ProductSpecifications.hasStatus(ProductStatus.LISTED);

        String term = blankToNull(query);
        if (term != null) {
            spec = spec.and(ProductSpecifications.nameContains(term));
        }
        if (minPrice != null) {
            spec = spec.and(ProductSpecifications.priceAtLeast(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecifications.priceAtMost(maxPrice));
        }
        if (categoryId != null) {
            spec = spec.and(ProductSpecifications.inCategory(categoryId));
        }
        if (subcategoryId != null) {
            spec = spec.and(ProductSpecifications.inSubcategory(subcategoryId));
        }
        if (restaurantId != null) {
            spec = spec.and(ProductSpecifications.fromRestaurant(restaurantId));
        }
        if (tagIds != null && !tagIds.isEmpty()) {
            spec = spec.and(ProductSpecifications.hasAnyTag(tagIds));
        }

        return PageResponse.of(products.findAll(spec, pageable), mapper::toSummary);
    }

    /** 根據使用者興趣推薦 — the legacy version hardcoded a developer's e-mail address. */
    @Transactional(readOnly = true)
    public PageResponse<ProductSummary> recommendedFor(Long userId, Pageable pageable) {
        return PageResponse.of(products.recommendedFor(userId, pageable), mapper::toSummary);
    }

    @Transactional(readOnly = true)
    public ProductDetail get(Long id, AuthenticatedUser caller) {
        Product product = products.findDetailedById(id).orElseThrow(() -> NotFoundException.of("商品", id));
        return mapper.toDetail(product, canEdit(product.getRestaurant(), caller));
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductSummary> listForRestaurant(Long restaurantId, Pageable pageable) {
        return PageResponse.of(products.findByRestaurantId(restaurantId, pageable), mapper::toSummary);
    }

    @Transactional
    public Long create(SaveProductRequest request, AuthenticatedUser caller) {
        Restaurant restaurant = requireOwnedRestaurant(request.restaurantId(), caller);

        Product product = new Product();
        product.setRestaurant(restaurant);
        apply(product, request);
        return products.save(product).getId();
    }

    @Transactional
    public void update(Long id, SaveProductRequest request, AuthenticatedUser caller) {
        Product product = products.findById(id).orElseThrow(() -> NotFoundException.of("商品", id));
        requireEditable(product, caller);
        apply(product, request);
    }

    @Transactional
    public void delete(Long id, AuthenticatedUser caller) {
        Product product = products.findById(id).orElseThrow(() -> NotFoundException.of("商品", id));
        requireEditable(product, caller);

        String image = product.getImagePath();
        products.delete(product);
        storage.delete(StorageBucket.PRODUCT_PHOTOS, image);
    }

    @Transactional
    public String uploadImage(Long id, MultipartFile file, AuthenticatedUser caller) {
        Product product = products.findById(id).orElseThrow(() -> NotFoundException.of("商品", id));
        requireEditable(product, caller);

        String previous = product.getImagePath();
        String path = storage.upload(StorageBucket.PRODUCT_PHOTOS, file);
        product.setImagePath(path);
        storage.delete(StorageBucket.PRODUCT_PHOTOS, previous);

        return mapper.imageUrl(path);
    }

    private void apply(Product product, SaveProductRequest request) {
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());

        if (request.subcategoryId() != null) {
            product.setSubcategory(subcategories
                    .findById(request.subcategoryId())
                    .orElseThrow(() -> NotFoundException.of("商品分類", request.subcategoryId())));
        } else {
            product.setSubcategory(null);
        }

        if (request.tagIds() != null) {
            product.setFoodTags(new LinkedHashSet<>(foodTags.findByIdIn(request.tagIds())));
        }

        if (request.listed()) {
            product.list();
        } else {
            product.delist();
        }
    }

    private Restaurant requireOwnedRestaurant(Long restaurantId, AuthenticatedUser caller) {
        Restaurant restaurant =
                restaurants.findById(restaurantId).orElseThrow(() -> NotFoundException.of("餐廳", restaurantId));
        if (!canEdit(restaurant, caller)) {
            throw new ForbiddenException("您沒有權限在這間餐廳下架設商品。");
        }
        return restaurant;
    }

    private void requireEditable(Product product, AuthenticatedUser caller) {
        if (!canEdit(product.getRestaurant(), caller)) {
            throw new ForbiddenException("您沒有權限修改這項商品。");
        }
    }

    private static boolean canEdit(Restaurant restaurant, AuthenticatedUser caller) {
        return caller != null && (restaurant.isOwnedBy(caller.id()) || caller.isAdmin());
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
