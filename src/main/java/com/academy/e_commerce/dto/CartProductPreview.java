package com.academy.e_commerce.dto;

import com.academy.e_commerce.model.Product;

public record CartProductPreview(
        Long id,
        Product product,
        Integer quantity
) {
}
