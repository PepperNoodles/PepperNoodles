package com.peppernoodles.shop.api;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.shop.api.dto.ProductDetail;
import com.peppernoodles.shop.api.dto.ProductSummary;
import com.peppernoodles.shop.api.dto.SaveProductRequest;
import com.peppernoodles.shop.domain.ProductCategory;
import com.peppernoodles.shop.repository.ProductCategoryRepository;
import com.peppernoodles.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/shop")
@Tag(name = "Shop", description = "商城：商品瀏覽、分類、商品管理")
public class ShopController {

    private final ProductService productService;
    private final ProductCategoryRepository categories;

    public ShopController(ProductService productService, ProductCategoryRepository categories) {
        this.productService = productService;
        this.categories = categories;
    }

    @GetMapping("/products")
    @Operation(
            summary = "商品搜尋與篩選",
            description = "One endpoint covering name, price range, category, restaurant and tag "
                    + "filters — replacing eleven near-duplicate legacy queries.")
    public PageResponse<ProductSummary> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) List<Long> tagIds,
            @PageableDefault(size = 12) Pageable pageable) {
        return productService.search(q, minPrice, maxPrice, categoryId, subcategoryId, restaurantId, tagIds, pageable);
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "商品詳細資料")
    public ProductDetail get(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        return productService.get(id, caller);
    }

    @GetMapping("/categories")
    @Operation(summary = "商品分類")
    public List<CategoryDto> categories() {
        return categories.findAllByOrderByNameAsc().stream()
                .map(ShopController::toCategoryDto)
                .toList();
    }

    @GetMapping("/recommended")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "依興趣推薦商品")
    public PageResponse<ProductSummary> recommended(
            @CurrentUser AuthenticatedUser caller, @PageableDefault(size = 12) Pageable pageable) {
        return productService.recommendedFor(caller.id(), pageable);
    }

    // --- owner-side product management ---------------------------------------

    @PostMapping("/products")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "新增商品")
    public ResponseEntity<Void> create(
            @Valid @RequestBody SaveProductRequest request, @CurrentUser AuthenticatedUser caller) {
        Long id = productService.create(request, caller);
        return ResponseEntity.created(URI.create("/api/v1/shop/products/" + id)).build();
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "修改商品")
    public void update(
            @PathVariable Long id,
            @Valid @RequestBody SaveProductRequest request,
            @CurrentUser AuthenticatedUser caller) {
        productService.update(id, request, caller);
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除商品")
    public void delete(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        productService.delete(id, caller);
    }

    @PostMapping(path = "/products/{id}/image", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "上傳商品圖片")
    public ImageUploaded uploadImage(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @CurrentUser AuthenticatedUser caller) {
        return new ImageUploaded(productService.uploadImage(id, file, caller));
    }

    private static CategoryDto toCategoryDto(ProductCategory c) {
        return new CategoryDto(
                c.getId(),
                c.getName(),
                c.getSubcategories().stream()
                        .map(s -> new CategoryDto.SubcategoryDto(s.getId(), s.getName()))
                        .toList());
    }

    public record CategoryDto(Long id, String name, List<SubcategoryDto> subcategories) {
        public record SubcategoryDto(Long id, String name) {}
    }

    public record ImageUploaded(String imageUrl) {}
}
