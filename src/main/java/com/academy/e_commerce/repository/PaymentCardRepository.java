package com.academy.e_commerce.repository;

import com.academy.e_commerce.model.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {
}
