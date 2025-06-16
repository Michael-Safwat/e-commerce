package com.academy.e_commerce.controller;

import com.academy.e_commerce.advice.UnauthorizedAccessException;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderRepository orderRepository;

    @PostMapping("/{userId}/pay/{orderId}")
    public ResponseEntity<Map<String, String>> createPaymentIntent(
            @PathVariable Long userId,
            @PathVariable Long orderId) throws StripeException {
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

        return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
    }

    // dummy endpoint for testing from backend
    @PostMapping("/{userId}/paytest/{orderId}")
    public ResponseEntity<String> simulatePaymentIntentSucceeded(@PathVariable Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            order.setStatus("PAID");
            orderRepository.save(order);
            return ResponseEntity.ok("Simulated payment_intent.succeeded for order ID: " + orderId);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found"));
    }

}

