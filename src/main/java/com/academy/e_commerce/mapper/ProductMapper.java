package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.ProductDTO;
import com.academy.e_commerce.model.Product;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductDTO productEntityToDto(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                product.getCategory(),
                product.getImage(),
                product.getRating()
                );
    }

    public static Product productDtoToEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .stock(dto.stock())
                .price(dto.price())
                .category(dto.category())
                .image(dto.image())
                .rating(dto.rating())
                .build();
    }
}
