package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.CartPreview;
import com.academy.e_commerce.dto.CartRequest;
import com.academy.e_commerce.service.cart_service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.endpoint.base-url}/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartCompositionService cartCompositionService;
    private final CartPreviewService cartPreviewService;
    private final CartItemAdjustmentService cartItemAdjustmentService;
    private final ClearCartService clearCartService;

    @PostMapping()
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<CartPreview> addProductToCart(
            @PathVariable("userId") Long customerId,
            @Valid @RequestBody AddToCartRequest request) {
        CartPreview cartDto = cartCompositionService.addNewProductToCart(customerId,request);
        return ResponseEntity.ok(cartDto);
    }

    @PatchMapping()
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<CartPreview> setProductQuantity(
            @PathVariable("userId") Long customerId,
            @RequestBody CartRequest request) {
        CartPreview cartDto = cartItemAdjustmentService.setProductQuantity(customerId, request.productId(), request.quantity());
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<CartPreview> getCartItems(@PathVariable("userId") Long userId) {
        CartPreview cart = cartPreviewService.getCartWithItems(userId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<Void> clearCart(@PathVariable("userId") Long userId) {
        clearCartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

}
