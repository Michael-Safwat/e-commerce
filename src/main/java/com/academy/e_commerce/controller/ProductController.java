package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.ProductDTO;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.endpoint.base-url}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

        // Create a new product using ProductDTO
        @PostMapping
        public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO) {
            return ResponseEntity.ok(productService.createProduct(productDTO));
        }

        // Get all products and return as DTOs
        // Search products with category and name filters + pagination
        @GetMapping("/search")
        public ResponseEntity<Page<Product>> getAllProducts(
                @RequestParam(name = "category", required = false) String category,
                @RequestParam(name = "name", required = false) String name,
                @PageableDefault(size = 10, page = 0) Pageable pageable) {
            return ResponseEntity.ok(productService.getAllProductsFiltered(category, name, pageable));
        }

        // Get all products with pagination
        @GetMapping
        public ResponseEntity<Page<Product>> getAllProducts(@PageableDefault(size = 10, page = 0) Pageable pageable) {
            return ResponseEntity.ok(productService.getAllProducts(pageable));
        }

        // Get a product by ID and return as DTO
        @GetMapping("/{id}")
        public ResponseEntity<Product> getProductById(@PathVariable Long id) {
            return ResponseEntity.ok(productService.getProductById(id));
        }

        // Update an existing product using DTO
        @PutMapping("/{id}")
        public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
            return ResponseEntity.ok(productService.updateProduct(id, productDTO));
        }

        // Delete a product by ID
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
}
