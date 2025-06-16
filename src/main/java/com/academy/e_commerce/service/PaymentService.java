package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.UnauthorizedAccessException;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public Map<String, String> createPaymentIntent(Long userId, Long orderId) throws StripeException {
        orderService.confirmOrder(userId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getUser() == null || !order.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You are not authorized to pay for this order.");
        }

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(BigDecimal.valueOf(order.getTotalPrice()).multiply(BigDecimal.valueOf(100)).longValue())
                .setCurrency("usd")
                .putMetadata("order_id", String.valueOf(orderId))
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return Map.of("clientSecret", intent.getClientSecret());
    }
}

