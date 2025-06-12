package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.ProductDTO;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

        // Create a new product using ProductDTO
        @PostMapping
        public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
            return ResponseEntity.ok(productService.createProduct(productDTO));
        }

        // Get all products and return as DTOs
        @GetMapping
        public ResponseEntity<List<ProductDTO>> getAllProducts() {
            return ResponseEntity.ok(productService.getAllProducts());
        }

        // Get a product by ID and return as DTO
        @GetMapping("/{id}")
        public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
            return productService.getProductById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }

        // Update an existing product using DTO
        @PutMapping("/{id}")
        public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
            return ResponseEntity.ok(productService.updateProduct(id, productDTO));
        }

        // Delete a product by ID
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
}
