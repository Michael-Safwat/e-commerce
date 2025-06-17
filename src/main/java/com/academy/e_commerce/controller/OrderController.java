package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.CartConfirmation;
import com.academy.e_commerce.dto.OrderConfirmationRequest;
import com.academy.e_commerce.dto.OrderResponse;
import com.academy.e_commerce.dto.OrderDTO;
import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.endpoint.base-url}/users/{userId}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<Page<OrderDTO>> getAllOrdersByCustomerId(
            @PathVariable("userId") Long userId,
            @PageableDefault(size = 10, page = 0) Pageable pageable){
        return ResponseEntity.ok(this.orderService.getAllOrdersByCustomerId(userId, pageable));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId){
        return ResponseEntity.ok(this.orderService.getOrderById(orderId, userId));
    }

    @PostMapping("/finalizeOrder")
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<CartConfirmation> checkoutOrder(@PathVariable("userId")Long userId, @RequestBody OrderConfirmationRequest request){
        return ResponseEntity.ok(this.orderService.finalizeOrder(userId,request));
    }

    //testing
//    @PostMapping("/testconfirm")
//    public void checkoutOrder2(@PathVariable("userId")Long userId){
//        this.orderService.confirmOrder(userId);
//    }
//
////    testing
//    @PostMapping("/testremove/{orderId}")
//    public void remove(@PathVariable("orderId")Long id){
//        this.orderService.removeOrder(id);
//    }
}
