package com.academy.e_commerce.dto;

public record ShippingAddressResponse(
        Long id,
        String street,
        String city,
        String state,
        String country
) {
}
