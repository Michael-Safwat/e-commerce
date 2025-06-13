package com.academy.e_commerce.repository;

import com.academy.e_commerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    Optional<Order> findByIdAndUser_Id(Long orderId, Long customerId);

    List<Order> findAllByUser_Id(Long customerId);
}
