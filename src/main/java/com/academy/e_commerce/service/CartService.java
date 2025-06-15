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
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart addProductToCart(Long userId, Long productId, Integer quantity) {

        log.debug("Adding product {} to cart for user {}", productId, userId);

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

        Cart finalCart = cart;
        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartProduct newCartProduct = CartProduct.builder()
                            .cart(finalCart)
                            .product(product)
                            .quantity(0)
                            .subPrice(0.0)
                            .build();
                    return newCartProduct;
                });

        cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
        cartProduct.setSubPrice(cartProduct.getQuantity() * product.getPrice());

        cartProductRepository.save(cartProduct);

        updateCartSubTotal(cart);
        cart.setItems(cartProductRepository.findByCart(cart));
        return cart;
    }



//    @Transactional
//    public CartDTO removeProductFromCart(Long userId, Long productId, Integer deducedQuantity) {
//
//        log.debug("Removing {} units of product {} from cart for user {}", deducedQuantity, productId, userId);
//
//        Cart cart = cartRepository.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));
//
//        CartProductId cartProductId = new CartProductId(cart.getId(), productId);
//        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
//                .orElseThrow(() -> new RuntimeException("Product not found in cart"));
//
//        int updatedQuantity = cartProduct.getQuantity() - deducedQuantity;
//        if (updatedQuantity <= 0) {
//            cartProductRepository.delete(cartProduct);
//        } else {
//            cartProduct.setQuantity(updatedQuantity);
//            cartProduct.setSubPrice(updatedQuantity * cartProduct.getProduct().getPrice());
//            cartProductRepository.save(cartProduct);
//        }
//
//        updateCartSubTotal(cart);
//
//        return CartMapper.toDto(cart);
//    }


    private void updateCartSubTotal(Cart cart) {
        double subTotal = cartProductRepository.findByCartId(cart.getId())
                .stream().mapToDouble(CartProduct::getSubPrice).sum();
        cart.setTotalPrice(subTotal);
        cartRepository.save(cart);
    }


}