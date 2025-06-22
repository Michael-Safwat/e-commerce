package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.BusinessException;
import com.academy.e_commerce.advice.PriceChangedException;
import com.academy.e_commerce.advice.UnauthorizedAccessException;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import static com.academy.e_commerce.utils.CartHelper.calculateOrderPrice;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public Map<String, String> createPaymentIntent(Long userId) throws StripeException {
        Long orderId = orderService.confirmOrder(userId);

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

        Double newPrice = calculateOrderPrice(order.getOrderProducts());
        if(!Objects.equals(order.getTotalPrice(), newPrice)){
            throw new BusinessException("the order total price changed as: "+order.getTotalPrice()+" and the new price is"+newPrice);
        }

        PaymentIntent intent = PaymentIntent.create(params);

        return Map.of("clientSecret", intent.getClientSecret());
    }


    public Map<String, String> createCheckoutSession(Long userId) throws StripeException {
        Long orderId = orderService.confirmOrder(userId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getUser() == null || !order.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You are not authorized to pay for this order.");
        }

        Double newPrice = calculateOrderPrice(order.getOrderProducts());
        if (!Objects.equals(order.getTotalPrice(), newPrice)) {
            throw new BusinessException("The order total price changed from " + order.getTotalPrice() + " to " + newPrice);
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8081/")
                .setCancelUrl("http://localhost:8081/")
                .setCustomerEmail(order.getUser().getEmail())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(BigDecimal.valueOf(order.getTotalPrice())
                                                        .multiply(BigDecimal.valueOf(100)).longValue())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Order #" + orderId)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        return Map.of("checkoutUrl", session.getUrl());
    }
}

