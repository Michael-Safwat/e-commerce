package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.model.Order;

public class OrderMapper {

    private OrderMapper(){}

    public static OrderDTO toDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getShippingAddress().getId(),
                order.getOrderProducts(),
                order.getCreatedAt()
        );
    }
}
