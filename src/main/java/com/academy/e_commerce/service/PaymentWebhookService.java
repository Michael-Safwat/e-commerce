package com.academy.e_commerce.service;

import com.academy.e_commerce.repository.OrderRepository;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final OrderRepository orderRepository;

    public void handleEvent(Event event) {
        switch (event.getType()) {
            case "payment_intent.succeeded":
                handlePaymentSucceeded(event);
                break;

            default:
                log.warn("Unhandled event type: {}", event.getType());
        }
    }

    private void handlePaymentSucceeded(Event event) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Failed to deserialize payment intent"));
        String orderId = intent.getMetadata().get("order_id");

        if (orderId != null) {
            orderRepository.findById(Long.parseLong(orderId)).ifPresent(order -> {
                order.setStatus("PAID");
                orderRepository.save(order);
                log.info("Order ID {} marked as PAID.", orderId);
            });
        } else {
            log.warn("No order_id found in payment intent metadata.");
        }
    }
}
