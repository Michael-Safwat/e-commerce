package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.cart.CartProductDTO;
import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.model.CartProduct;

public class CartProductMapper {

    private CartProductMapper() {
    }

    public static CartProductDTO toDto(CartProduct cartProduct) {
        return new CartProductDTO(
                cartProduct.getId(),
                cartProduct.getProduct(),
                cartProduct.getSubPrice(),
                cartProduct.getQuantity()
        );
    }

    public static CartProduct toEntity(CartProductDTO dto, Cart cart) {
        return CartProduct.builder()
                .id(dto.id())
                .product(dto.product())
                .subPrice(dto.subPrice())
                .quantity(dto.quantity())
                .cart(cart)
                .build();
    }
}

