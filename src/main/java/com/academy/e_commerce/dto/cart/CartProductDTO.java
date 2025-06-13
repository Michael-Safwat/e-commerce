package com.academy.e_commerce.dto.cart;

import com.academy.e_commerce.model.CartProductId;
import com.academy.e_commerce.model.Product;

public record CartProductDTO (
        CartProductId id,
        Product product,
        Double subPrice,
        Integer quantity
){}
