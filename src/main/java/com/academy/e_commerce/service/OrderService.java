package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.InsufficientStockException;
import com.academy.e_commerce.advice.OrderNotFoundException;
import com.academy.e_commerce.dto.OrderConfirmationRequest;
import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.mapper.CartToOrderMapper;
import com.academy.e_commerce.mapper.OrderMapper;
import com.academy.e_commerce.model.*;
import com.academy.e_commerce.repository.OrderRepository;
import com.academy.e_commerce.repository.ProductRepository;
import com.academy.e_commerce.service.cart_service.CartPreviewService;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartPreviewService cartPreviewService;
    private final UserService userService;
    private final ClearCartService clearCartService;
    private final ProductRepository productRepository;


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
    public Order checkoutOrder(Long userId , OrderConfirmationRequest request) {

        Cart cart = this.cartPreviewService.getCartWithItems(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot proceed with checkout.");
        }

        // Validate stock and deduce qty
        for (CartProduct cartProduct : cart.getItems()) {
            Product product = productRepository.findById(cartProduct.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            validateStock(product, cartProduct.getQuantity());

            product.setStock(product.getStock() - cartProduct.getQuantity());
            productRepository.save(product);
        }

        Set<OrderProduct> orderProducts;
        Order order = new Order();
        orderProducts = cart.getItems().stream().map(cartProduct ->
                CartToOrderMapper.CartProductToOrderProduct(cartProduct, order))
                .collect(Collectors.toSet());

        order.setStatus("PENDING");
        order.setUser(this.userService.getUserById(userId));
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress(request.shippingAddress());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderProducts(orderProducts);

        clearCartService.clearCart(cart.getId());

        return this.orderRepository.save(order);
    }

    private void validateStock(Product product, int requestedQuantity) {
        if (product.getStock() < requestedQuantity) {
            throw new InsufficientStockException(product.getId(), requestedQuantity, product.getStock());
        }
    }

}
