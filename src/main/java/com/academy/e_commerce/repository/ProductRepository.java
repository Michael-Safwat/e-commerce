package com.academy.e_commerce.repository;

import com.academy.e_commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds products by category and name (case-insensitive for both).
     * The 'Containing' keyword acts as a LIKE operator for partial matches.
     *
     * @param category The category to search for.
     * @param name     The product name (or part of it) to search for.
     * @return A list of matching products.
     */
    List<Product> findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase(String category, String name);

    /**
     * Finds products by category (case-insensitive).
     *
     * @param category The category to search for.
     * @return A list of matching products.
     */
    List<Product> findByCategoryContainingIgnoreCase(String category);

    /**
     * Finds products by name (case-insensitive).
     *
     * @param name The product name (or part of it) to search for.
     * @return A list of matching products.
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}
