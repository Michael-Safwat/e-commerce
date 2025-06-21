package com.academy.e_commerce.dto;

import java.util.List;

public record CartConfirmation(
        Long cartId,
        Long userId,
        List<CartProductPreview> items,
        Double totalPrice,
        ShippingAddressResponse shippingAddress
) {}
