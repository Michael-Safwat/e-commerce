package com.academy.e_commerce.order;

import com.academy.e_commerce.user.User;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    private User user;
}
