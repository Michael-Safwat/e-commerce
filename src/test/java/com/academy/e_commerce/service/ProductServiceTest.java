package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.ProductDTO;
import com.academy.e_commerce.mapper.ProductMapper;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

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
        return product;
    }

    private ProductDTO getSampleProductDTO() {
        return new ProductDTO(
                "Sample Product",
                "A sample product",
                10,
                19.99,
                "Books",
                "image.png",
                4
                );
    }


    @Test
    void testCreateProduct() {
        Product product = ProductMapper.productDtoToEntity(getSampleProductDTO());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product saved = productService.createProduct(getSampleProductDTO());

        assertNotNull(saved);
        assertEquals("Sample Product", saved.getName());
        verify(productRepository).save(any(Product.class));
    }


    @Test
    void testGetAllProductsFiltered_withCategoryAndName() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = getSampleProduct();
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase("Books", "Sample", pageable))
                .thenReturn(page);

        Page<Product> result = productService.getAllProductsFiltered("Books", "Sample", pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepository).findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase("Books", "Sample", pageable);
    }

    @Test
    void testGetAllProductsFiltered_onlyCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = getSampleProduct();
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findByCategoryContainingIgnoreCase("Books", pageable)).thenReturn(page);

        Page<Product> result = productService.getAllProductsFiltered("Books", null, pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepository).findByCategoryContainingIgnoreCase("Books", pageable);
    }

    @Test
    void testGetAllProductsFiltered_onlyName() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = getSampleProduct();
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findByNameContainingIgnoreCase("Sample", pageable)).thenReturn(page);

        Page<Product> result = productService.getAllProductsFiltered(null, "Sample", pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepository).findByNameContainingIgnoreCase("Sample", pageable);
    }

    @Test
    void testGetAllProductsFiltered_noFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = getSampleProduct();
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> result = productService.getAllProductsFiltered(null, null, pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepository).findAll(pageable);
    }

    @Test
    void testGetProductById_found() {
        Product product = getSampleProduct();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product found = productService.getProductById(1L);

        assertNotNull(found);
        assertEquals("Sample Product", found.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductById_notFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                productService.getProductById(999L));

        assertEquals("Product not found for ID: 999", ex.getMessage());
    }

    @Test
    void testUpdateProduct_found() {
        Product existingProduct = getSampleProduct();
        Product updatedProduct = ProductMapper.productDtoToEntity(getSampleProductDTO());
        updatedProduct.setId(existingProduct.getId());

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, getSampleProductDTO());

        assertEquals("Sample Product", result.getName());
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_notFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                productService.updateProduct(999L, getSampleProductDTO()));
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }
}
