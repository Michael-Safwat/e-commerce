package com.academy.e_commerce.service.cart_service;

import com.academy.e_commerce.model.*;
import com.academy.e_commerce.repository.CartProductRepository;
import com.academy.e_commerce.repository.CartRepository;
import com.academy.e_commerce.repository.ProductRepository;
import com.academy.e_commerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddProductToCartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final UserRepository userRepository;
    private final CartHelperService cartHelperService;

    @Transactional
    public Cart addNewProductToCart(Long userId, Long productId, Integer quantity) {
        log.debug("Adding new product {} to cart for user {}", productId, userId);

        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));

            cart = Cart.builder()
                    .user(user)
                    .totalPrice(0.0)
                    .items(new ArrayList<>())
                    .build();

            cart = cartRepository.save(cart);
            user.setCart(cart);
            userRepository.save(user);
        }

        if (cart.getId() == null) {
            throw new RuntimeException("Cart ID is null after save, something went wrong!");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartHelperService.validateStock(product, quantity);

        CartProduct cartProduct = CartProduct.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .subPrice(quantity * product.getPrice())
                .build();

        cartProductRepository.save(cartProduct);
        cartHelperService.updateCartSubTotal(cart);

        cart.setItems(cartProductRepository.findByCart(cart));

        return cart;
    }

}