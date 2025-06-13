package com.academy.e_commerce.dto;


public record ProductDTO(
        Long id,
        String name,
        String description,
        Integer stock,
        Double price,
        String category,
        String image,
        Double rating
) {}
