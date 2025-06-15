package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.OrderNotFoundException;
import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.mapper.CartToOrderMapper;
import com.academy.e_commerce.mapper.OrderMapper;
import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.model.OrderProduct;
import com.academy.e_commerce.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartPreviewService cartPreviewService;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, CartPreviewService cartPreviewService, UserService userService) {
        this.orderRepository = orderRepository;
        this.cartPreviewService = cartPreviewService;
        this.userService = userService;
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

    @Transactional
    public Order checkoutOrder(Long userId, Long cartId) {

        // to place an order, we first need to get the products from the cart
        // then create a new order and add products to it, and add all arguments to it
        // clear the current cart
        // save the order

        List<CartProduct> cartProducts = this.cartPreviewService.getCartItems(userId);

        Set<OrderProduct> orderProducts;
        Order order = new Order();
        orderProducts = cartProducts.stream().map(cartProduct ->
                CartToOrderMapper.CartProductToOrderProduct(cartProduct, order))
                .collect(Collectors.toSet());


        order.setStatus("PENDING");
        order.setUser(this.userService.getUserById(userId));
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress("ay7aga");
        order.setTotalPrice(100.0);
        order.setOrderProducts(orderProducts);

        return this.orderRepository.save(order);
    }
}
