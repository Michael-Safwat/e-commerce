package com.academy.e_commerce.dto;

import java.time.LocalDateTime;

public record OrderDTO(
        Long id,
        String status,
        Double totalPrice,
        String shippingAddress,
        LocalDateTime createdAt
) {}
