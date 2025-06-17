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
    private final OrderService orderService;

    public void handleEvent(Event event) {
        String eventType = event.getType();
        log.info("Received Stripe event: {}", eventType);
        switch (event.getType()) {
            case "payment_intent.succeeded":
                handlePaymentSucceeded(event);
                break;
            case "payment_intent.payment_failed":
                handlePaymentFailed(event);
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
        if (orderId != null){
            updateOrderStatus(orderId, "PAID", "Payment succeeded");
        } else {
            log.warn("No order_id found in payment intent succeeded metadata.");
        }
    }

    private void handlePaymentFailed(Event event) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Failed to deserialize payment intent"));

        String orderId = intent.getMetadata().get("order_id");

        if (orderId != null) {
            long parsedOrderId = Long.parseLong(orderId);
            updateOrderStatus(orderId, "FAILED", "Payment failed");
            orderService.removeOrder(parsedOrderId);
        } else {
            log.warn("No order_id found in payment intent failed metadata.");
        }
    }

    private void updateOrderStatus(String orderId, String status, String logMessage) {
        if (orderId != null) {
            orderRepository.findById(Long.parseLong(orderId)).ifPresent(order -> {
                order.setStatus(status);
                orderRepository.save(order);
                log.info("Order ID {} marked as {}. {}", orderId, status, logMessage);
            });
        } else {
            log.warn("No order_id found in metadata for status update.");
        }
    }
}
