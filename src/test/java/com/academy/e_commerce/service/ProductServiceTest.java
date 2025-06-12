package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.ProductDTO;
import com.academy.e_commerce.mapper.ProductMapper;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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

    private ProductDTO getSampleProductDTO() {
        return ProductDTO.builder()
                .name("Phone")
                .description("Smartphone")
                .stock(10)
                .price(599.99)
                .category("Electronics")
                .image("image.jpg")
                .rating(5)
                .build();
    }

    private Product getSampleProductEntity() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Phone");
        product.setDescription("Smartphone");
        product.setStock(10);
        product.setPrice(599.99);
        product.setCategory("Electronics");
        product.setImage("image.jpg");
        product.setRating(5);
        return product;
    }


    @Test
    void testCreateProduct() {
        ProductDTO dto = getSampleProductDTO();
        Product entity = ProductMapper.productDtoToEntity(dto);
        entity.setId(1L);

        when(productRepository.save(any(Product.class))).thenReturn(entity);

        ProductDTO result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getPrice(), result.getPrice());
    }

    @Test
    void testGetProductById_found() {
        Product product = getSampleProductEntity();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<ProductDTO> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Phone", result.get().getName());
    }

    @Test
    void testGetProductById_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ProductDTO> result = productService.getProductById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllProducts_noFilter() {
        List<Product> products = List.of(getSampleProductEntity());
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> result = productService.getAllProducts(null, null);

        assertEquals(1, result.size());
        assertEquals("Phone", result.get(0).getName());
    }

    @Test
    void testUpdateProduct_success() {
        Product existing = getSampleProductEntity();
        ProductDTO update = getSampleProductDTO();
        update.setName("Updated Phone");

        Product updatedEntity = ProductMapper.productDtoToEntity(update);
        updatedEntity.setId(existing.getId());

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(updatedEntity);

        ProductDTO result = productService.updateProduct(1L, update);

        assertEquals("Updated Phone", result.getName());
    }

    @Test
    void testUpdateProduct_notFound() {
        ProductDTO update = getSampleProductDTO();
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                productService.updateProduct(1L, update));

        assertEquals("Product not found", ex.getMessage());
    }

    @Test
    void testDeleteProduct() {
        Long id = 1L;

        productService.deleteProduct(id);

        verify(productRepository, times(1)).deleteById(id);
    }
}
