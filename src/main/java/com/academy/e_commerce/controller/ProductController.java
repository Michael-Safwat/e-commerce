package com.academy.e_commerce.controller;

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

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "name", required = false) String name,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProductsFiltered(category, name, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}
