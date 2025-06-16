package com.academy.e_commerce.service.cart_service;

import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.CartProductRepository;
import com.academy.e_commerce.repository.CartRepository;
import com.academy.e_commerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCartItemsService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final CartHelperService cartHelperService;

    @Transactional
    public Cart increaseProductQuantityInCart(Long userId, Long productId, Integer quantity) {
        log.debug("Updating quantity for product {} in cart for user {}", productId, userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));



        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        Integer newQuantity = quantity + cartProduct.getQuantity();

        cartHelperService.validateStock(product, newQuantity);

        cartProduct.setQuantity(newQuantity);

        cartProductRepository.save(cartProduct);
        cartHelperService.updateCartSubTotal(cart);

        cart.setItems(cartProductRepository.findByCart(cart));

        return cart;
    }

    @Transactional
    public Cart decreaseProductQuantityInCart(Long userId, Long productId, Integer quantity) {
        log.debug("Updating quantity for product {} in cart for user {}", productId, userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));



        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        Integer newQuantity= cartProduct.getQuantity() - quantity;

        if(newQuantity <= 0)
            newQuantity = 0;

        cartProduct.setQuantity(newQuantity);

        cartProductRepository.save(cartProduct);
        cartHelperService.updateCartSubTotal(cart);

        cart.setItems(cartProductRepository.findByCart(cart));

        return cart;
    }

    @Transactional
    public Cart setProductQuantity(Long userId, Long productId, Integer quantity) {
        log.debug("Setting quantity for product {} in cart for user {}", productId, userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        cartHelperService.validateStock(product, quantity);

        cartProduct.setQuantity(quantity);

        cartProductRepository.save(cartProduct);
        cartHelperService.updateCartSubTotal(cart);

        cart.setItems(cartProductRepository.findByCart(cart));

        return cart;
    }


}
