package com.academy.e_commerce.mapper;

import com.academy.e_commerce.dto.cart.CartDTO;
import com.academy.e_commerce.dto.cart.CartProductDTO;
import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.model.CartProduct;

import java.util.List;

public class CartMapper {

    private CartMapper() {
    }

    public static CartDTO toDto(Cart cart) {
        List<CartProductDTO> itemsDto = cart.getItems().stream()
                .map(CartProductMapper::toDto)
                .toList();

        return new CartDTO(cart.getId(), cart.getSubPrice(), itemsDto);
    }

    public static Cart toEntity(CartDTO cartDto) {
        Cart cart = Cart.builder()
                .id(cartDto.id())
                .subPrice(cartDto.subPrice())
                .build();

        List<CartProduct> items = cartDto.items().stream()
                .map(itemDto -> CartProductMapper.toEntity(itemDto, cart))
                .toList();

        cart.setItems(items);

        return cart;
    }
}
