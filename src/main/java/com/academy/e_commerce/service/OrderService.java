package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.InsufficientStockException;
import com.academy.e_commerce.advice.OrderNotFoundException;
import com.academy.e_commerce.dto.OrderConfirmationRequest;
import com.academy.e_commerce.dto.OrderResponse;
import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.mapper.CartToOrderMapper;
import com.academy.e_commerce.mapper.OrderMapper;
import com.academy.e_commerce.mapper.OrderToOrderResponseMapper;
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


    public Cart finalizeOrder(Long userId , OrderConfirmationRequest request) {
        Cart cart = this.cartPreviewService.getCartWithItems(userId);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot proceed with checkout.");
        }
        cart.setShippingAddress(request.shippingAddress());
        return cart;
    }

    @Transactional
    public void confirmOrder(Long userId) {
        Cart cart = this.cartPreviewService.getCartWithItems(userId);

        // Validate stock and deduce qty
        for (CartProduct cartProduct : cart.getItems()) {
            Product product = productRepository.findByIdWithLock(cartProduct.getProduct().getId())
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
        order.setTotalPrice(cart.getTotalPrice());
        order.setShippingAddress(cart.getShippingAddress());
        order.setOrderProducts(orderProducts);

        clearCartService.clearCart(userId);
        orderRepository.save(order);
    }

    //todo compare pay amount to cart total price -> throw exception


    private void validateStock(Product product, int requestedQuantity) {
        if (product.getStock() < requestedQuantity) {
            throw new InsufficientStockException(product.getId(), requestedQuantity, product.getStock());
        }
    }

}
