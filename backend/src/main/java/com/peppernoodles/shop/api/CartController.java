package com.peppernoodles.shop.api;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.shop.api.dto.CartDto;
import com.peppernoodles.shop.api.dto.UpdateCartItemRequest;
import com.peppernoodles.shop.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Cart", description = "購物車")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "檢視購物車", description = "Each line reports whether it is still purchasable.")
    public CartDto cart(@CurrentUser AuthenticatedUser caller) {
        return cartService.getCart(caller.id());
    }

    @PutMapping("/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "加入或更新購物車項目", description = "Sets the absolute quantity for a product.")
    public void putItem(
            @Valid @RequestBody UpdateCartItemRequest request, @CurrentUser AuthenticatedUser caller) {
        cartService.putItem(caller.id(), request.productId(), request.quantity());
    }

    @DeleteMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "移除購物車項目")
    public void removeItem(@PathVariable Long productId, @CurrentUser AuthenticatedUser caller) {
        cartService.removeItem(caller.id(), productId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "清空購物車")
    public void clear(@CurrentUser AuthenticatedUser caller) {
        cartService.clear(caller.id());
    }
}
