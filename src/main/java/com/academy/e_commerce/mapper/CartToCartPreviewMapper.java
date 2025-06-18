package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.CartPreview;
import com.academy.e_commerce.model.Cart;

import static com.academy.e_commerce.utils.CartHelper.calculateTotalPrice;

public class CartToCartPreviewMapper {

    private CartToCartPreviewMapper() {
    }

    public static CartPreview toPreview(Cart cart) {
        return new CartPreview(
                cart.getId(),
                cart.getUser() != null ? cart.getUser().getId() : null,
                cart.getItems(),
                calculateTotalPrice(cart.getItems())
        );
    }


}
