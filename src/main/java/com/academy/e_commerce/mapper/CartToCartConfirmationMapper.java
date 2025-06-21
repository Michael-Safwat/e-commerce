package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.CartConfirmation;
import com.academy.e_commerce.model.Cart;

import static com.academy.e_commerce.utils.CartHelper.calculateTotalPrice;

public class CartToCartConfirmationMapper {

    private CartToCartConfirmationMapper() {
    }

    public static CartConfirmation toConfirmation(Cart cart) {
        return new CartConfirmation(
                cart.getId(),
                cart.getUser() != null ? cart.getUser().getId() : null,
                cart.getItems(),
                calculateTotalPrice(cart.getItems()),
                cart.getShippingAddress().toString()
        );
    }
}
