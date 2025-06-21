package com.academy.e_commerce.dto;

import com.academy.e_commerce.model.OrderProduct;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Long id,
        String status,
        Double totalPrice,
        Long shippingAddress_id,
        List<OrderProduct> items,
        LocalDateTime createdAt
) {}
