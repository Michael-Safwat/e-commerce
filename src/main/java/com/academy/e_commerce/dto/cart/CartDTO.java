package com.academy.e_commerce.dto.cart;

import java.util.List;

public record CartDTO (
        Long id,
        Double subPrice,
        List<CartProductDTO>items
        ){}
