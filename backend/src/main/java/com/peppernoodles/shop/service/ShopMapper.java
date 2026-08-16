package com.peppernoodles.shop.service;

import com.peppernoodles.common.storage.StorageBucket;
import com.peppernoodles.common.storage.StorageService;
import com.peppernoodles.restaurant.api.dto.TagSummary;
import com.peppernoodles.shop.api.dto.OrderDto;
import com.peppernoodles.shop.api.dto.ProductDetail;
import com.peppernoodles.shop.api.dto.ProductSummary;
import com.peppernoodles.shop.domain.Order;
import com.peppernoodles.shop.domain.Product;
import com.peppernoodles.shop.domain.ProductSubcategory;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {

    private final StorageService storage;

    public ShopMapper(StorageService storage) {
        this.storage = storage;
    }

    public ProductSummary toSummary(Product p) {
        ProductSubcategory sub = p.getSubcategory();
        return new ProductSummary(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getQuantity(),
                imageUrl(p.getImagePath()),
                p.getStatus().name(),
                p.getRestaurant().getId(),
                p.getRestaurant().getName(),
                sub == null ? null : sub.getCategory().getName(),
                sub == null ? null : sub.getName(),
                tags(p));
    }

    public ProductDetail toDetail(Product p, boolean editable) {
        ProductSubcategory sub = p.getSubcategory();
        return new ProductDetail(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getQuantity(),
                imageUrl(p.getImagePath()),
                p.getStatus().name(),
                p.getRestaurant().getId(),
                p.getRestaurant().getName(),
                sub == null ? null : sub.getCategory().getName(),
                sub == null ? null : sub.getName(),
                tags(p),
                editable,
                p.getReleasedAt());
    }

    public OrderDto toOrder(Order o) {
        List<OrderDto.OrderLine> lines = o.getItems().stream()
                .map(i -> new OrderDto.OrderLine(
                        i.getProduct() == null ? null : i.getProduct().getId(),
                        i.getProductName(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.lineTotal()))
                .toList();

        return new OrderDto(
                o.getId(), o.getOrderNo(), o.getStatus().name(), o.getTotalCost(),
                o.getReceiverName(), o.getReceiverPhone(), o.getReceiverAddress(),
                o.getCreatedAt(), o.getPaidAt(), o.getExpiresAt(), lines);
    }

    public String imageUrl(String path) {
        return storage.publicUrl(StorageBucket.PRODUCT_PHOTOS, path);
    }

    private static List<TagSummary> tags(Product p) {
        return p.getFoodTags().stream()
                .map(TagSummary::from)
                .sorted(Comparator.comparing(TagSummary::name))
                .toList();
    }
}
