package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.OrderNotFoundException;
import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.mapper.OrderMapper;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDTO getOrderById(Long orderId) {
        Optional<Order> order = this.orderRepository.findById(orderId);

        if(order.isPresent())
            return OrderMapper.orderToOrderDTO(order.get());
        else
            throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
    }
}
