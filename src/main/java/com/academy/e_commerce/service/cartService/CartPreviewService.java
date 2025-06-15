package com.academy.e_commerce.service.cartService;

import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.repository.CartProductRepository;
import com.academy.e_commerce.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartPreviewService {

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    @Transactional
    public Cart getCartWithItems(Long userId) {
        log.debug("Fetching cart for user {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));

        cart.setItems(cartProductRepository.findByCart(cart));

        return cart;
    }

}
