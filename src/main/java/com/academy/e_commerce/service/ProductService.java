package com.academy.e_commerce.service;

import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
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
