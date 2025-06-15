package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.cart.CartDTO;
import com.academy.e_commerce.dto.cart.CartRequest;
import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.service.CartPreviewService;
import com.academy.e_commerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartPreviewService cartPreviewService;


    @PostMapping
    //@PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable("userId") Long customerId,
            @RequestBody CartRequest request) {
        Cart cartDto = cartService.addProductToCart(customerId, request.productId(), request.quantity());
        return ResponseEntity.ok(cartDto);
    }

//    @DeleteMapping
//    //@PreAuthorize("authentication.principal.claims['userId'] == #userId")
//    public ResponseEntity<CartDTO> removeProductFromCart(
//            @PathVariable("userId") Long userId,
//            @RequestBody CartRequest request) {
//
//        CartDTO cartDto = cartService.removeProductFromCart(userId, request.productId(), request.quantity());
//        return ResponseEntity.ok(cartDto);
//    }

    @GetMapping
//    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<List<CartProduct>> getCartItems(@PathVariable("userId") Long userId) {
        List<CartProduct> items = cartPreviewService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }
}
