package com.peppernoodles.shop.service;

import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.shop.api.dto.CartDto;
import com.peppernoodles.shop.domain.CartItem;
import com.peppernoodles.shop.domain.Product;
import com.peppernoodles.shop.repository.CartItemRepository;
import com.peppernoodles.shop.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 購物車. Persisted per user rather than held in the session. */
@Service
public class CartService {

    private final CartItemRepository cartItems;
    private final ProductRepository products;
    private final ShopMapper mapper;

    public CartService(CartItemRepository cartItems, ProductRepository products, ShopMapper mapper) {
        this.cartItems = cartItems;
        this.products = products;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public CartDto getCart(Long userId) {
        List<CartItem> items = cartItems.findByUserId(userId);

        List<CartDto.CartLine> lines = items.stream()
                .map(item -> {
                    Product p = item.getProduct();
                    // A product can be delisted or sell out after it was added, so
                    // every line reports whether it is still purchasable.
                    boolean unavailable = !p.isListed() || p.getQuantity() < item.getQuantity();
                    return new CartDto.CartLine(
                            p.getId(),
                            p.getName(),
                            mapper.imageUrl(p.getImagePath()),
                            p.getPrice(),
                            item.getQuantity(),
                            p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())),
                            p.getQuantity(),
                            unavailable);
                })
                .toList();

        BigDecimal total = lines.stream()
                .filter(l -> !l.unavailable())
                .map(CartDto.CartLine::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartDto(lines, total, lines.stream().anyMatch(CartDto.CartLine::unavailable));
    }

    @Transactional
    public void putItem(Long userId, Long productId, int quantity) {
        Product product = products.findById(productId).orElseThrow(() -> NotFoundException.of("商品", productId));

        if (!product.isListed()) {
            throw new ConflictException("「%s」目前未上架。".formatted(product.getName()));
        }
        if (product.getQuantity() < quantity) {
            throw new ConflictException("「%s」庫存不足，目前僅剩 %d 件。".formatted(product.getName(), product.getQuantity()));
        }

        cartItems
                .findByUserIdAndProductId(userId, productId)
                .ifPresentOrElse(
                        existing -> existing.setQuantity(quantity),
                        () -> cartItems.save(new CartItem(userId, productId, quantity)));
    }

    @Transactional
    public void removeItem(Long userId, Long productId) {
        cartItems.findByUserIdAndProductId(userId, productId).ifPresent(cartItems::delete);
    }

    @Transactional
    public void clear(Long userId) {
        cartItems.clearCart(userId);
    }
}
