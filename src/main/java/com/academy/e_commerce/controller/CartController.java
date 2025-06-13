package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.cart.CartDTO;
import com.academy.e_commerce.dto.cart.CartRequest;
import com.academy.e_commerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.endpoint.base-url}/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable("userId") Long customerId,
            @RequestBody CartRequest request) {
        CartDTO cartDto = cartService.addProductToCart(customerId, request.productId(), request.quantity());
        return ResponseEntity.ok(cartDto);
    }
}
