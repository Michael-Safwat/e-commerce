package com.academy.e_commerce.payment;

import com.academy.e_commerce.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardHolder;

    @Column(name = "masked_card_number", nullable = false, length = 19)
    private String maskedCardNumber;

    @NotNull(message = "Expiry month cannot be null")
    @Min(value = 1, message = "Expiry month must be between 1 and 12")
    @Max(value = 12, message = "Expiry month must be between 1 and 12")
    @Column(name = "expiry_month", nullable = false)
    private Integer expiryMonth;

    @NotNull(message = "Expiry year cannot be null")
    @Min(value = 2024, message = "Expiry year must be a valid future year")
    @Column(name = "expiry_year", nullable = false)
    private Integer expiryYear;

    @ManyToOne
    private User customer;
}
