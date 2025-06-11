package com.academy.e_commerce.product;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String name;

    private String description;

    private Integer stock;

    private Double price;

    private String category;

    private String Image;

    private int rating;
}
