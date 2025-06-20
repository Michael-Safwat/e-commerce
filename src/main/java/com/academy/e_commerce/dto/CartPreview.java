package com.academy.e_commerce.dto;

import com.academy.e_commerce.model.CartProduct;

import java.util.List;

public record CartPreview(
        Long id,
        Long userId,
        List<CartProductPreview> items,
        Double totalPrice
) {}

