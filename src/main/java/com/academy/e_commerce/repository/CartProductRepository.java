package com.academy.e_commerce.repository;

import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.model.CartProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, CartProductId> {
    List<CartProduct> findByCartId(Long cartId);
}
