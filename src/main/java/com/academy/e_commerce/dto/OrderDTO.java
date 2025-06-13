package com.academy.e_commerce.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private String status;
    private BigDecimal totalPrice;
    private String shippingAddress;
    private String paymentMethod;
    private LocalDateTime createdAt;
}
