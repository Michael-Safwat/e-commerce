package com.academy.e_commerce.dto;

import com.academy.e_commerce.model.CartProduct;

import java.util.List;

public record CartConfirmation(
        Long cartId,
        Long userId,
        List<CartProduct> items,
        Double totalPrice,
        String shippingAddress
) {}
