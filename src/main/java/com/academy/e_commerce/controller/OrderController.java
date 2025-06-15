package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.endpoint.base-url}/customers/{customerId}/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<Page<OrderDTO>> getAllOrdersByCustomerId(
            @PathVariable("customerId") Long customerId,
            @PageableDefault(size = 10, page = 0) Pageable pageable){
        return ResponseEntity.ok(this.orderService.getAllOrdersByCustomerId(customerId, pageable));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") Long orderId, @PathVariable("customerId") Long customerId){
        return ResponseEntity.ok(this.orderService.getOrderById(orderId, customerId));
    }

    @PostMapping("/checkoutOrder/{cartId}")
    @PreAuthorize("authentication.principal.claims['userId'] == #customerId")
    public ResponseEntity<OrderDTO> checkoutOrder(@PathVariable("cartId")Long cartId){
        return ResponseEntity.ok(this.orderService.checkoutOrder(cartId));
    }
}
