package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.ProductDTO;
import com.academy.e_commerce.mapper.ProductMapper;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

        // Create a new product using ProductDTO and return Product
        public Product createProduct(ProductDTO productDTO) {
            log.info("Creating new product: {}", productDTO.name());
            Product product = ProductMapper.productDtoToEntity(productDTO);
            return productRepository.save(product);
        }

        // Get all products filtered with pagination (returns Product instead of DTO)
        public Page<Product> getAllProductsFiltered(String category, String name, Pageable pageable) {
            log.info("Fetching products with filters: category={}, name={}", category, name);

            Page<Product> products;
            boolean hasCategory = StringUtils.hasText(category);
            boolean hasName = StringUtils.hasText(name);

            if (hasCategory && hasName) {
                products = productRepository.findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase(category, name, pageable);
            } else if (hasCategory) {
                products = productRepository.findByCategoryContainingIgnoreCase(category, pageable);
            } else if (hasName) {
                products = productRepository.findByNameContainingIgnoreCase(name, pageable);
            } else {
                products = productRepository.findAll(pageable);
            }
            return products; // Returns Product instead of mapping to DTO
        }

        // Get all products with pagination (returns Product)
        public Page<Product> getAllProducts(Pageable pageable) {
            log.info("Fetching all products with pagination");
            return productRepository.findAll(pageable);
        }

        // Get a product by ID (returns Product)
        public Product getProductById(Long id) {
            log.info("Fetching product by ID: {}", id);
            return productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found for ID: " + id));
        }

        // Update an existing product using ProductDTO and return Product
        public Product updateProduct(Long id, ProductDTO updatedProductDTO) {
            log.info("Updating product ID: {}", id);
            return productRepository.findById(id).map(product -> {
                Product updatedProduct = ProductMapper.productDtoToEntity(updatedProductDTO);
                updatedProduct.setId(product.getId()); // Preserve ID
                return productRepository.save(updatedProduct);
            }).orElseThrow(() -> new RuntimeException("Product not found"));
        }

        // Delete a product by ID
        public void deleteProduct(Long id) {
            log.info("Deleting product ID: {}", id);
            productRepository.deleteById(id);
        }
}
