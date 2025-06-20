package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.CartProductPreview;
import com.academy.e_commerce.model.CartProduct;

public class CartProductMapper {

    public static CartProductPreview toPreview(CartProduct cartProduct) {
        return new CartProductPreview(
                cartProduct.getId(),
                cartProduct.getProduct(),
                cartProduct.getQuantity()
        );
    }
}

