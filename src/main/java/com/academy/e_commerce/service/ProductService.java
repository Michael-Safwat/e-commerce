package com.academy.e_commerce.service;

import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    // Create a new product
    public Product createProduct(Product product) {
        log.info("Creating new product: {}", product.getName());
        return productRepository.save(product);
    }

    // Get all products
    public List<Product> getAllProducts(String category, String name) {
        log.info("Fetching products");
        List<Product> products;

        // Use StringUtils.hasText() to check if the string is non-null and has actual text (not just whitespace)
        boolean hasCategory = StringUtils.hasText(category);
        boolean hasName = StringUtils.hasText(name);

        if (hasCategory && hasName) {
            // Case 1: Both category and name filters are present
            products = productRepository.findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase(category, name);
        } else if (hasCategory) {
            // Case 2: Only category filter is present
            products = productRepository.findByCategoryContainingIgnoreCase(category);
        } else if (hasName) {
            // Case 3: Only name filter is present
            products = productRepository.findByNameContainingIgnoreCase(name);
        } else {
            // Case 4: No filters are present, retrieve all products
            products = productRepository.findAll();
        }
        return products;
    }

    // Get a product by ID
    public Optional<Product> getProductById(Long id) {
        log.info("Fetching product by ID: {}", id);
        return productRepository.findById(id);
    }

    // Update an existing product
    public Product updateProduct(Long id, Product updatedProduct) {
        log.info("Updating product ID: {}", id);
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setStock(updatedProduct.getStock());
            product.setPrice(updatedProduct.getPrice());
            product.setCategory(updatedProduct.getCategory());
            product.setImage(updatedProduct.getImage());
            product.setRating(updatedProduct.getRating());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Delete a product by ID
    public void deleteProduct(Long id) {
        log.info("Deleting product ID: {}", id);
        productRepository.deleteById(id);
    }
}
