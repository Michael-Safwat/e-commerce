package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.model.Order;

public class OrderMapper {

    private OrderMapper(){}

    public static OrderDTO orderToOrderDTO(Order order){
        return OrderDTO.builder()
                .id(order.getId())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
