package com.academy.e_commerce.controller;

import com.academy.e_commerce.repository.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;


@RestController
@RequestMapping("/api/webhook")
public class PaymentWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final OrderRepository orderRepository;

    public PaymentWebhookController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                String orderId = intent.getMetadata().get("order_id");

                if (orderId != null) {
                    orderRepository.findById(Long.parseLong(orderId)).ifPresent(order -> {
                        order.setStatus("PAID");
                        orderRepository.save(order);
                    });
                }
            }

            return ResponseEntity.ok("");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook signature invalid");
        }
    }
}

