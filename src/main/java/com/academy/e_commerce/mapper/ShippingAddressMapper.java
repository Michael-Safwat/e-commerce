package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.ShippingAddressResponse;
import com.academy.e_commerce.model.ShippingAddress;

public class ShippingAddressMapper {

    private ShippingAddressMapper() {
    }

    public static ShippingAddressResponse toResponse(ShippingAddress address) {
        return new ShippingAddressResponse(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry()
        );
    }
}
