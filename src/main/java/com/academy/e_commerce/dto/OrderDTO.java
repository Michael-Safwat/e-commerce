package com.academy.e_commerce.dto;


import com.academy.e_commerce.model.PaymentCard;
import com.academy.e_commerce.model.ShippingAddress;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDTO(
        Long id,
        String status,
        BigDecimal totalPrice,
        ShippingAddress shippingAddress,
        PaymentCard paymentMethod,
        LocalDateTime createdAt
) {}
