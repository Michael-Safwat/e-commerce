package com.academy.e_commerce.controller;

import com.academy.e_commerce.advice.UnauthorizedAccessException;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.repository.OrderRepository;
import com.academy.e_commerce.service.OrderService;
import com.academy.e_commerce.service.PaymentService;
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
    private final PaymentService paymentService;

    @PostMapping("/{userId}/pay")
    public ResponseEntity<Map<String, String>> createPaymentIntent(
            @PathVariable Long userId) throws StripeException {

        Map<String, String> response = paymentService.createCheckoutSession(userId);
        return ResponseEntity.ok(response);
    }
}

