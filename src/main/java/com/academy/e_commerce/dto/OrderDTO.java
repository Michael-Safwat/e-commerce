package com.academy.e_commerce.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDTO(
        Long id,
        String status,
        BigDecimal totalPrice,
        String shippingAddress,
        String paymentMethod,
        LocalDateTime createdAt
) {}
