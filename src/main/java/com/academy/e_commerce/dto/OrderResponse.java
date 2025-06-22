package com.academy.e_commerce.dto;

import com.academy.e_commerce.model.OrderProduct;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        String status,
        Double totalPrice,
        Long shippingAddress,
        LocalDateTime createdAt,
        List<OrderProduct> orderProducts
){}
