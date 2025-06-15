package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.OrderNotFoundException;
import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.mapper.OrderMapper;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Page<OrderDTO> getAllOrdersByCustomerId(Long customerId, Pageable pageable) {
        Page<Order> orders = this.orderRepository.findAllByUser_Id(customerId, pageable);
        return orders.map(OrderMapper::orderToOrderDTO);
    }

    public OrderDTO getOrderById(Long orderId, Long customerId) {
        Optional<Order> order = this.orderRepository.findByIdAndUser_Id(orderId, customerId);

        if(order.isPresent())
            return OrderMapper.orderToOrderDTO(order.get());
        else
            throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
    }


    public OrderDTO checkoutOrder(Long cartId) {

        return null;
    }
}
