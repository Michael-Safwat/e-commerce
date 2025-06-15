package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.cart.CartDTO;
import com.academy.e_commerce.mapper.CartMapper;
import com.academy.e_commerce.model.*;
import com.academy.e_commerce.repository.CartProductRepository;
import com.academy.e_commerce.repository.CartRepository;
import com.academy.e_commerce.repository.ProductRepository;
import com.academy.e_commerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartDTO addProductToCart(Long userId, Long productId, Integer quantity) {

        log.debug("Adding product {} to cart for user {}", productId, userId);

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));

            Cart newCart = Cart.builder()
                    .user(user)
                    .subPrice(0.0)
                    .build();
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartProductId cartProductId = new CartProductId(cart.getId(), productId);
        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                .orElseGet(() -> new CartProduct(cartProductId, cart, product, 0, 0.0));

        cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
        cartProduct.setSubPrice(cartProduct.getQuantity() * product.getPrice());

        cartProductRepository.save(cartProduct);

        updateCartSubTotal(cart);

        return CartMapper.toDto(cart);
    }

    private void updateCartSubTotal(Cart cart) {
        double subTotal = cartProductRepository.findByCartId(cart.getId())
                .stream().mapToDouble(CartProduct::getSubPrice).sum();
        cart.setSubPrice(subTotal);
        cartRepository.save(cart);
    }
}