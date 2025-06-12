package com.academy.e_commerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String status;
    private BigDecimal totalPrice;
    private String shippingAddress;
    private String paymentMethod;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderProduct> orderItems = new HashSet<>();

    public void addOrderItem(OrderProduct orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void removeOrderItem(OrderProduct orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }
}
