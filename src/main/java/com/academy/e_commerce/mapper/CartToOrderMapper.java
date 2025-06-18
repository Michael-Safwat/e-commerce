package com.academy.e_commerce.mapper;

import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.model.Order;
import com.academy.e_commerce.model.OrderProduct;

public class CartToOrderMapper {

    private CartToOrderMapper(){}

    public static OrderProduct CartProductToOrderProduct(CartProduct cartProduct, Order order){

        return OrderProduct.builder()
                .order(order)
                .product(cartProduct.getProduct())
                .quantity(cartProduct.getQuantity())
                .build();
    }
}
