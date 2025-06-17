package com.academy.e_commerce.service.cart_service;

import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.repository.CartProductRepository;
import com.academy.e_commerce.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClearCartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    @Transactional
    public void clearCart(Long userId) {
        log.debug("Clearing cart for user {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));

        cartProductRepository.deleteByCart(cart.getId());

        cart.setItems(new ArrayList<>());
        cartRepository.save(cart);
    }


}
