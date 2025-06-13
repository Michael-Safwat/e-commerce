package com.academy.e_commerce.dto;


public record ProductDTO(
        String name,
        String description,
        Integer stock,
        Double price,
        String category,
        String image,
        int rating
) {}
