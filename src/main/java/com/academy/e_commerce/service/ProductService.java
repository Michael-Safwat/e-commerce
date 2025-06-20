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
                throw new ImageUploadException("Failed to upload image to S3", e);
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
            return products;
        }

        public Page<Product> getAllProducts(Pageable pageable) {
            return productRepository.findAll(pageable);
        }

        public Product getProductById(Long id) {
            log.debug("Fetching product by ID: {}", id);
            return productRepository.findById(id)
                    .orElseThrow(() -> new BusinessException("Product not found for ID: " + id));
        }

    @Transactional
        public Product updateProduct(Long id, ProductDTO updatedProductDTO) {
            log.debug("Updating product ID: {}", id);
            Product product = productRepository.findByIdWithLock(id).orElseThrow(() -> new BusinessException("Product not found for ID: " + id));
            product.setName(updatedProductDTO.name());
            product.setDescription(updatedProductDTO.description());
            product.setStock(updatedProductDTO.stock());
            product.setPrice(updatedProductDTO.price());
            product.setCategory(updatedProductDTO.category());
            product.setImage(updatedProductDTO.image());
            product.setRating(updatedProductDTO.rating());
        return productRepository.save(product);
        }

        public void deleteProduct(Long id) {
            log.info("Deleting product ID: {}", id);
            productRepository.deleteById(id);
        }
}
