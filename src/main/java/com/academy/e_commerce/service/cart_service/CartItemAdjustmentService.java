package com.academy.e_commerce.service.cart_service;

import com.academy.e_commerce.advice.BusinessException;
import com.academy.e_commerce.dto.CartPreview;
import com.academy.e_commerce.mapper.CartToCartPreviewMapper;
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

import java.util.List;

import static com.academy.e_commerce.utils.CartHelper.validateStock;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemAdjustmentService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    @Transactional
    public CartPreview setProductQuantity(Long userId, Long productId, Integer quantity) {
        log.debug("Setting quantity for product {} in cart for user {}", productId, userId);

        Cart cart = cartRepository.findWithWriteLockByUserId(userId)
                .orElseThrow(() -> new BusinessException("Cart not found for user ID: " + userId));

        Product product = productRepository.findWithSharedLockById(productId)
                .orElseThrow(() -> new BusinessException("Product not found"));

        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new BusinessException("Product not found in cart"));

        if (quantity == 0) {
            cart.getItems().remove(cartProduct);
            cartProductRepository.delete(cartProduct);
        } else {
            validateStock(product, quantity);
            cartProduct.setQuantity(quantity);
            cartProductRepository.save(cartProduct);
        }

        List<CartProduct> updatedItems = cartProductRepository.findByCart(cart);
        if (updatedItems.isEmpty()) {
            throw new BusinessException("Your cart is empty");
        }

        cart.setItems(updatedItems);
        return CartToCartPreviewMapper.toPreview(cart);
    }


}
