package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.OrderNotFoundException;
import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.mapper.CartToOrderMapper;
import com.academy.e_commerce.mapper.OrderMapper;
import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.model.OrderProduct;
import com.academy.e_commerce.repository.OrderRepository;
import com.academy.e_commerce.service.cart_service.CartPreviewService;
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

    // todo: locking for race condition on creating order
    @Transactional
    public Order checkoutOrder(Long userId) {

        // get user cart
        Cart cart = this.cartPreviewService.getCartWithItems(userId);

        // populate user products from cart
        Set<OrderProduct> orderProducts;
        Order order = new Order();
        orderProducts = cart.getItems().stream().map(cartProduct ->
                CartToOrderMapper.CartProductToOrderProduct(cartProduct, order))
                .collect(Collectors.toSet());

        order.setStatus("PENDING");
        order.setUser(this.userService.getUserById(userId));
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress("ay7aga");
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderProducts(orderProducts);

        // todo: update products stock

        // todo: clear cart

        // create order
        return this.orderRepository.save(order);
    }
}
