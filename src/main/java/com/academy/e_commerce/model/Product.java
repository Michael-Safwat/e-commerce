package com.academy.e_commerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotNull(message = "Description can't be empty")
    private String description;

    @NotBlank(message = "Stock can't be empty")
    private Integer stock;

    @NotBlank(message = "Price can't be empty")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Category can't be empty")
    private String category;

    @NotNull(message = "Image can't be empty")
    private String image;

    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

}
