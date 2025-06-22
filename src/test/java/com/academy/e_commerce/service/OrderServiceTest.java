package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.BusinessException;
import com.academy.e_commerce.dto.OrderConfirmationRequest;
import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.mapper.OrderMapper;
import com.academy.e_commerce.model.*;
import com.academy.e_commerce.repository.CartRepository;
import com.academy.e_commerce.repository.OrderRepository;
import com.academy.e_commerce.repository.ProductRepository;
import com.academy.e_commerce.service.cart_service.ClearCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private ClearCartService clearCartService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Order getSampleOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress("123 Street");
        order.setTotalPrice(100.0);
        order.setUser(new User());
        order.setOrderProducts(new ArrayList<>());
        return order;
    }

    @Test
    void testGetAllOrdersByCustomerId_success() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Order order = getSampleOrder();

        when(orderRepository.findAllByUser_Id(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(order)));

        Page<OrderDTO> result = orderService.getAllOrdersByCustomerId(userId, pageable);

        assertEquals(1, result.getTotalElements());
        verify(orderRepository).findAllByUser_Id(userId, pageable);
    }

    @Test
    void testGetOrderById_found() {
        Long userId = 1L;
        Long orderId = 1L;
        Order order = getSampleOrder();

        when(orderRepository.findByIdAndUser_Id(orderId, userId)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderById(orderId, userId);

        assertEquals(orderId, result.id());
        verify(orderRepository).findByIdAndUser_Id(orderId, userId);
    }

    @Test
    void testGetOrderById_notFound() {
        Long userId = 1L;
        Long orderId = 999L;

        when(orderRepository.findByIdAndUser_Id(orderId, userId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> orderService.getOrderById(orderId, userId));

        assertTrue(ex.getMessage().contains("Order with ID"));
    }

    @Test
    void testFinalizeOrder_success() {
        Long userId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setPrice(99.99);

        CartProduct cartProduct = new CartProduct();
        cartProduct.setQuantity(2);
        cartProduct.setProduct(product);

        Cart cart = new Cart();
        cart.setUser(new User());
        cart.setItems(List.of(cartProduct));

        OrderConfirmationRequest request = new OrderConfirmationRequest("123 Street");

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        var result = orderService.finalizeOrder(userId, request);

        assertEquals("123 Street", result.shippingAddress());

        verify(cartRepository).save(cart);
    }


    @Test
    void testFinalizeOrder_cartNotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> orderService.finalizeOrder(1L, new OrderConfirmationRequest("")));
    }

    @Test
    void testFinalizeOrder_cartEmpty() {
        Cart emptyCart = new Cart();
        emptyCart.setItems(Collections.emptyList());

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(emptyCart));

        assertThrows(BusinessException.class,
                () -> orderService.finalizeOrder(1L, new OrderConfirmationRequest("")));
    }

    @Test
    void testRemoveOrder_success() {
        Long orderId = 1L;
        Product product = new Product();
        product.setId(1L);
        product.setStock(5);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(2);

        Order order = new Order();
        order.setOrderProducts(List.of(orderProduct));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findByIdWithLock(1L)).thenReturn(Optional.of(product));

        orderService.removeOrder(orderId);

        assertEquals(7, product.getStock());
        verify(productRepository).save(product);
    }

    @Test
    void testRemoveOrder_orderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> orderService.removeOrder(1L));
    }

    @Test
    void testRemoveOrder_productNotFound() {
        Product product = new Product();
        product.setId(1L);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(1);

        Order order = new Order();
        order.setOrderProducts(List.of(orderProduct));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findByIdWithLock(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> orderService.removeOrder(1L));
    }
}
