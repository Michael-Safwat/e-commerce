package com.academy.e_commerce.repository;

import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.model.CartProductId;
import com.academy.e_commerce.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, CartProductId> {
    List<CartProduct> findByCartId(Long cartId);
    Optional<CartProduct> findByCartAndProduct(Cart cart, Product product);
    List<CartProduct> findByCart(Cart cart);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_product WHERE cart_id = :cartId", nativeQuery = true)
    void deleteByCart(@Param("cartId") Long cartId);


}
