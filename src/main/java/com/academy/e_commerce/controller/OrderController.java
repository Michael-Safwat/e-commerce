package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/customers/{customerId}/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByCustomerId(@PathVariable("customerId") Long customerId){
        return ResponseEntity.ok(this.orderService.getAllOrdersByCustomerId(customerId));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") Long orderId, @PathVariable("customerId") Long customerId){
        return ResponseEntity.ok(this.orderService.getOrderById(orderId, customerId));
    }
}
