package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.UnauthorizedAccessException;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.stubbing.Answer;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Order getSampleOrder(Long userId) {
        Order order = new Order();
        order.setId(1L);
        order.setTotalPrice(99.99);
        User user = new User();
        user.setId(userId);
        order.setUser(user);
        return order;
    }

//    @Test
//    void testCreatePaymentIntent_success() throws StripeException {
//        Long userId = 1L;
//        Long orderId = 1L;
//        Order order = getSampleOrder(userId);
//
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//        doNothing().when(orderService).confirmOrder(userId);
//
//        // Mock static PaymentIntent.create() method
//        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
//            PaymentIntent fakeIntent = mock(PaymentIntent.class);
//            when(fakeIntent.getClientSecret()).thenReturn("secret_abc123");
//            mockedPaymentIntent.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
//                    .thenReturn(fakeIntent);
//
//            Map<String, String> result = paymentService.createPaymentIntent(userId, orderId);
//
//            assertEquals("secret_abc123", result.get("clientSecret"));
//            verify(orderService).confirmOrder(userId);
//            verify(orderRepository).findById(orderId);
//        }
//    }
//
//    @Test
//    void testCreatePaymentIntent_orderNotFound() {
//        Long userId = 1L;
//        Long orderId = 1L;
//
//        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
//
//        assertThrows(IllegalArgumentException.class, () ->
//                paymentService.createPaymentIntent(userId, orderId));
//
//        verify(orderRepository).findById(orderId);
//    }
//
//    @Test
//    void testCreatePaymentIntent_unauthorizedAccess() {
//        Long userId = 1L;
//        Long orderId = 1L;
//
//        Order order = getSampleOrder(2L); // different user ID
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//
//        assertThrows(UnauthorizedAccessException.class, () ->
//                paymentService.createPaymentIntent(userId, orderId));
//    }
}
