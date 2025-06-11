package com.academy.e_commerce.dto;

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

    private String image;

    private int rating;
}
