package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.ProductDTO;
import com.academy.e_commerce.mapper.ProductMapper;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.ProductRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public Product createProduct(ProductDTO productDTO, MultipartFile imageFile) {
        log.info("Creating new product: {}", productDTO.name());
        Product product = ProductMapper.productDtoToEntity(productDTO);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String key = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(imageFile.getSize());
                metadata.setContentType(imageFile.getContentType());

                s3Client.putObject(bucketName, key, imageFile.getInputStream(), metadata);
                String imageUrl = s3Client.getUrl(bucketName, key).toString();

                product.setImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image to S3", e);
            }
        }

        return productRepository.save(product);
    }


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

        public Page<Product> getAllProducts(Pageable pageable) {
            log.info("Fetching all products with pagination");
            return productRepository.findAll(pageable);
        }

        public Product getProductById(Long id) {
            log.info("Fetching product by ID: {}", id);
            return productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found for ID: " + id));
        }

        public Product updateProduct(Long id, ProductDTO updatedProductDTO) {
            log.info("Updating product ID: {}", id);
            return productRepository.findById(id).map(product -> {
                Product updatedProduct = ProductMapper.productDtoToEntity(updatedProductDTO);
                updatedProduct.setId(product.getId()); // Preserve ID
                return productRepository.save(updatedProduct);
            }).orElseThrow(() -> new RuntimeException("Product not found"));
        }

        public void deleteProduct(Long id) {
            log.info("Deleting product ID: {}", id);
            productRepository.deleteById(id);
        }
}
