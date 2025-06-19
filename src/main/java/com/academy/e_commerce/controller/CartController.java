package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.CartPreview;
import com.academy.e_commerce.dto.CartRequest;
import com.academy.e_commerce.service.cart_service.AddToCartRequest;
import com.academy.e_commerce.service.cart_service.CartPreviewService;
import com.academy.e_commerce.service.cart_service.CartCompositionService;
import com.academy.e_commerce.service.cart_service.UpdateCartItemsService;
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
    private final UpdateCartItemsService updateCartItemsService;


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
        CartPreview cartDto = updateCartItemsService.setProductQuantity(customerId, request.productId(), request.quantity());
        return ResponseEntity.ok(cartDto);
    }

    @PatchMapping("/increase")
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<CartPreview> increaseProductQuantity(
            @PathVariable("userId") Long customerId,
            @RequestBody CartRequest request) {
        CartPreview cartDto = updateCartItemsService.increaseProductQuantityInCart(customerId, request.productId(), request.quantity());
        return ResponseEntity.ok(cartDto);
    }

    @PatchMapping("/decrease")
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<CartPreview> decreaseProductQuantity(
            @PathVariable("userId") Long customerId,
            @RequestBody CartRequest request) {
        CartPreview cartDto = updateCartItemsService.decreaseProductQuantityInCart(customerId, request.productId(), request.quantity());
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<CartPreview> getCartItems(@PathVariable("userId") Long userId) {
        CartPreview cart = cartPreviewService.getCartWithItems(userId);
        return ResponseEntity.ok(cart);
    }
}
