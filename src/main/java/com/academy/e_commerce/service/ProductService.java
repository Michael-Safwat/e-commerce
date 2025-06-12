package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.ProductDTO;
import com.academy.e_commerce.mapper.ProductMapper;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // Create a new product using ProductDTO
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getName());
        Product product = ProductMapper.productDtoToEntity(productDTO);
        return ProductMapper.productEntityToDto(productRepository.save(product));
    }

    // Get all products and convert to ProductDTO
    public List<ProductDTO> getAllProductsFiltered(String category, String name) {
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

        // Convert Product list to ProductDTO list using ProductMapper
        return products.stream()
                .map(ProductMapper::productEntityToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllProducts (){
        log.info("Fetching all products");
        return productRepository.findAll().stream()
                .map(ProductMapper::productEntityToDto)
                .collect(Collectors.toList());
    }

    // Get a product by ID and convert to ProductDTO
    public Optional<ProductDTO> getProductById(Long id) {
        log.info("Fetching product by ID: {}", id);
        return productRepository.findById(id).map(ProductMapper::productEntityToDto);
    }

    // Update an existing product using ProductDTO
    public ProductDTO updateProduct(Long id, ProductDTO updatedProductDTO) {
        log.info("Updating product ID: {}", id);
        return productRepository.findById(id).map(product -> {
            Product updatedProduct = ProductMapper.productDtoToEntity(updatedProductDTO);
            updatedProduct.setId(product.getId()); // Ensure the ID remains the same
            return ProductMapper.productEntityToDto(productRepository.save(updatedProduct));
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Delete a product by ID
    public void deleteProduct(Long id) {
        log.info("Deleting product ID: {}", id);
        productRepository.deleteById(id);
    }
}
