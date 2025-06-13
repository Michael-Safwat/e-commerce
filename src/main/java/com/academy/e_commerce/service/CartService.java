package com.academy.e_commerce.service;

import com.academy.e_commerce.model.Cart;
import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.model.CartProductId;
import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.repository.CartProductRepository;
import com.academy.e_commerce.repository.CartRepository;
import com.academy.e_commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;


}
