package com.academy.e_commerce.dto;


import java.util.List;

public record CartPreview(
        Long id,
        Long userId,
        List<CartProductPreview> items,
        Double totalPrice
) {}

