package com.academy.e_commerce.utils;

import com.academy.e_commerce.advice.InsufficientStockException;
import com.academy.e_commerce.model.CartProduct;
import com.academy.e_commerce.model.Product;

import java.util.List;

public class CartHelper {

    private CartHelper() {
    }

    public static Double calculateTotalPrice(List<CartProduct> items) {
        return items.stream()
                .mapToDouble(cartProduct -> cartProduct.getQuantity() * cartProduct.getProduct().getPrice()) // Compute price dynamically
                .sum();
    }

    public static void validateStock(Product product, int requestedQuantity) {
        if (product.getStock() < requestedQuantity) {
            throw new InsufficientStockException(product.getId(), requestedQuantity, product.getStock());
        }
    }
}
