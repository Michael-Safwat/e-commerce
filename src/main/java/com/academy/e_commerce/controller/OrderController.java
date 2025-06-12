package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.service.OrderService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<OrderDTO>> getAllOrdersByCustomerId(@PathVariable("customerId") Long customerId){
        return ResponseEntity.ok(this.orderService.getAllOrdersByCustomerId(customerId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") Long orderId, @PathVariable("customerId") Long customerId){
        return ResponseEntity.ok(this.orderService.getOrderById(orderId, customerId));
    }
}
