package com.academy.e_commerce.advice;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long productId, int requestedQuantity, int availableStock) {
        super("Insufficient stock for product ID: " + productId +
                ". Requested: " + requestedQuantity + ", Available: " + availableStock);
    }
}

