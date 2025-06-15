package com.academy.e_commerce.repository;

import com.academy.e_commerce.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndUser_Id(Long orderId, Long customerId);
    Page<Order> findAllByUser_Id(Long customerId, Pageable pageable);
}
