package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.BusinessException;
import com.academy.e_commerce.advice.ImageUploadException;
import com.academy.e_commerce.dto.ProductDTO;
import com.academy.e_commerce.mapper.ProductMapper;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.ProductRepository;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AmazonS3 s3Client;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Product getSampleProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
        product.setCategory("Books");
        product.setPrice(19.99);
        product.setImage("https://example.com/sample.jpg");
        return product;
    }

    private ProductDTO getSampleProductDTO() {
        return new ProductDTO(
                1L,
                "Sample Product",
                "A sample product",
                10,
                19.99,
                "Books",
                "https://example.com/sample.jpg",
                4.0
        );
    }


    @Test
    void testCreateProduct_imageUploadFails_throwsImageUploadException() throws IOException {
        ProductDTO dto = getSampleProductDTO();
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(imageFile.getOriginalFilename()).thenReturn("image.jpg");
        when(imageFile.getSize()).thenReturn(123L);
        when(imageFile.getContentType()).thenReturn("image/jpeg");
        when(imageFile.getInputStream()).thenThrow(new IOException("Simulated failure"));

        assertThrows(ImageUploadException.class, () -> productService.createProduct(dto, imageFile));

        verify(productRepository, never()).save(any());
    }

    @Test
    void testGetAllProductsFiltered_noFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(getSampleProduct()));

        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> result = productService.getAllProductsFiltered(null, null, pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepository).findAll(pageable);
    }

    @Test
    void testGetProductById_found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(getSampleProduct()));

        Product found = productService.getProductById(1L);

        assertNotNull(found);
        assertEquals("Sample Product", found.getName());
    }

    @Test
    void testGetProductById_notFound_throwsBusinessException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> productService.getProductById(1L));
        assertEquals("Product not found for ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateProduct_found_successfulUpdate() {
        Product existing = getSampleProduct();
        ProductDTO dto = getSampleProductDTO();
        existing.setName("Old Name");

        when(productRepository.findByIdWithLock(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updated = productService.updateProduct(1L, dto);

        assertEquals("Sample Product", updated.getName());
        verify(productRepository).findByIdWithLock(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_notFound_throwsBusinessException() {
        when(productRepository.findByIdWithLock(99L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> productService.updateProduct(99L, getSampleProductDTO()));
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }
}
