package com.academy.e_commerce.product;

public class ProductMapper {
    public static ProductDTO ProductEntityToDTO(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDTO.builder()
                .name(product.getName())
                .description(product.getDescription())
                .stock(product.getStock())
                .price(product.getPrice())
                .category(product.getCategory())
                .image(product.getImage())
                .rating(product.getRating())
                .build();
    }

    public static Product ProductDTOToEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .stock(dto.getStock())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .image(dto.getImage())
                .rating(dto.getRating())
                .build();
    }
}
