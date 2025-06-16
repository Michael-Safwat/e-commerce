package com.academy.e_commerce.dto;

public record CartRequest(
        Long productId,
        Integer quantity
) {}
