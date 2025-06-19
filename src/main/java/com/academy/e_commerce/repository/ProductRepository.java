package com.academy.e_commerce.repository;

import com.academy.e_commerce.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products WHERE id = :productId FOR UPDATE", nativeQuery = true)
    Optional<Product> findByIdWithLock(@Param("productId") Long productId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Product> findWithSharedLockById(Long id);

    /**
     * Finds products by category and name (case-insensitive for both).
     * The 'Containing' keyword acts as a LIKE operator for partial matches.
     *
     * @param category The category to search for.
     * @param name     The product name (or part of it) to search for.
     * @return A list of matching products.
     */
    Page<Product> findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase(String category, String name , Pageable pageable);

    /**
     * Finds products by category (case-insensitive).
     *
     * @param category The category to search for.
     * @return A list of matching products.
     */
    Page<Product>  findByCategoryContainingIgnoreCase(String category, Pageable pageable);

    /**
     * Finds products by name (case-insensitive).
     *
     * @param name The product name (or part of it) to search for.
     * @return A list of matching products.
     */
    Page<Product>  findByNameContainingIgnoreCase(String name, Pageable pageable);
}
