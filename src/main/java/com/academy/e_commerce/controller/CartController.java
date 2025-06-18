package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.CartPreview;
import com.academy.e_commerce.dto.CartRequest;
import com.academy.e_commerce.service.cart_service.CartPreviewService;
import com.academy.e_commerce.service.cart_service.AddProductToCartService;
import com.academy.e_commerce.service.cart_service.UpdateCartItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.endpoint.base-url}/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {
    private final AddProductToCartService addProductToCartService;
    private final CartPreviewService cartPreviewService;
    private final UpdateCartItemsService updateCartItemsService;


    @PostMapping()
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<CartPreview> addProductToCart(
            @PathVariable("userId") Long customerId,
            @RequestBody CartRequest request) {
        CartPreview cartDto = addProductToCartService.addNewProductToCart(customerId, request.productId(), request.quantity());
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
