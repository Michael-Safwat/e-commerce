package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.OrderResponse;
import com.academy.e_commerce.model.Order;

public class OrderToOrderResponseMapper {

    private OrderToOrderResponseMapper() {
    }

    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getShippingAddress(),
                order.getCreatedAt(),
                order.getOrderProducts()
        );
    }
}
