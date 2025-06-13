package com.academy.e_commerce.dto.cart;

public record CartRequest(
        Long productId,
        Integer quantity
) {}
