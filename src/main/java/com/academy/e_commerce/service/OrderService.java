package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.OrderNotFoundException;
import com.academy.e_commerce.dto.CartConfirmation;
import com.academy.e_commerce.dto.OrderConfirmationRequest;
import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.mapper.CartToCartConfirmationMapper;
import com.academy.e_commerce.mapper.CartToOrderMapper;
import com.academy.e_commerce.mapper.OrderMapper;
import com.academy.e_commerce.model.*;
import com.academy.e_commerce.repository.CartRepository;
import com.academy.e_commerce.repository.OrderRepository;
import com.academy.e_commerce.repository.ProductRepository;
import com.academy.e_commerce.service.cart_service.ClearCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.academy.e_commerce.utils.CartHelper.calculateTotalPrice;
import static com.academy.e_commerce.utils.CartHelper.validateStock;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ClearCartService clearCartService;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;


    public Page<OrderDTO> getAllOrdersByCustomerId(Long customerId, Pageable pageable) {
        Page<Order> orders = this.orderRepository.findAllByUser_Id(customerId, pageable);
        return orders.map(OrderMapper::toDTO);
    }

    public OrderDTO getOrderById(Long orderId, Long customerId) {
        Optional<Order> order = this.orderRepository.findByIdAndUser_Id(orderId, customerId);

        if(order.isPresent())
            return OrderMapper.toDTO(order.get());
        else
            throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
    }


    public CartConfirmation finalizeOrder(Long userId , OrderConfirmationRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot proceed with checkout.");
        }
        cart.setShippingAddress(request.shippingAddress());
        cartRepository.save(cart);
        return CartToCartConfirmationMapper.toConfirmation(cart);
    }

    @Transactional
    public void confirmOrder(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));

        // Validate stock and deduce qty
        for (CartProduct cartProduct : cart.getItems()) {
            Product product = productRepository.findByIdWithLock(cartProduct.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            validateStock(product, cartProduct.getQuantity());

            product.setStock(product.getStock() - cartProduct.getQuantity());
            productRepository.save(product);
        }

        List<OrderProduct> orderProducts;
        Order order = new Order();
        orderProducts = cart.getItems().stream().map(cartProduct ->
                CartToOrderMapper.CartProductToOrderProduct(cartProduct, order))
                .collect(Collectors.toList());

        order.setStatus("PENDING");
        order.setUser(this.userService.getUserById(userId));
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(calculateTotalPrice(cart.getItems()));
        order.setShippingAddress(cart.getShippingAddress());
        order.setOrderProducts(orderProducts);

        clearCartService.clearCart(userId);
        orderRepository.save(order);
    }

    @Transactional
    public void removeOrder(Long orderId) {
        log.debug("Rolling back products of order {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = productRepository.findByIdWithLock(orderProduct.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setStock(product.getStock() + orderProduct.getQuantity());
            productRepository.save(product);
        }

    }

}
