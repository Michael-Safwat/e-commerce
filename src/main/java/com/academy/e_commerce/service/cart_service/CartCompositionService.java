package com.academy.e_commerce.service.cart_service;

import com.academy.e_commerce.advice.BusinessException;
import com.academy.e_commerce.dto.CartPreview;
import com.academy.e_commerce.mapper.CartToCartPreviewMapper;
import com.academy.e_commerce.model.*;
import com.academy.e_commerce.repository.CartProductRepository;
import com.academy.e_commerce.repository.CartRepository;
import com.academy.e_commerce.repository.ProductRepository;
import com.academy.e_commerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.academy.e_commerce.utils.CartHelper.validateStock;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartCompositionService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartPreview addNewProductToCart(Long userId,@Valid AddToCartRequest request) {
        log.debug("Adding new product {} to cart for user {}", request.getProductId(), userId);

        Cart cart = findOrCreateCart(userId);

        boolean productExists = cart.getItems().stream()
                .anyMatch(cp -> cp.getProduct().getId().equals(request.getProductId()));

        if (productExists) {
            throw new BusinessException("Product already exists in the cart");
        }

        Product product = fetchProductWithValidation(request.getProductId(), request.getQuantity());

        CartProduct cartProduct = CartProduct.builder()
                .cart(cart)
                .product(product)
                .quantity(request.getQuantity())
                .build();

        cartProductRepository.save(cartProduct);
        cart.setItems(cartProductRepository.findByCart(cart));

        return CartToCartPreviewMapper.toPreview(cart);
    }

    private Cart findOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> createCart(userId));
    }


    private Cart createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User isn't found with id: " + userId));

        Cart newCart = Cart.builder()
                .user(user)
                .items(new ArrayList<>())
                .build();

        Cart savedCart = cartRepository.save(newCart);
        user.setCart(savedCart);
        userRepository.save(user);

        return savedCart;
    }

    private Product fetchProductWithValidation(Long productId, Integer quantity) {

        Product product = productRepository.findWithSharedLockById(productId)
                .orElseThrow(() -> new BusinessException("Product isn't found"));
        validateStock(product, quantity);
        return product;
    }
}