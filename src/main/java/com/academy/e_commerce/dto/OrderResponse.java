package com.academy.e_commerce.dto;

import com.academy.e_commerce.model.OrderProduct;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderResponse(
        Long id,
        Long userId,
        String status,
        Double totalPrice,
        String shippingAddress,
        LocalDateTime createdAt,
        Set<OrderProduct> orderProducts
){}
