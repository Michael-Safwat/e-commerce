package com.academy.e_commerce.advice;

public class PriceChangedException extends RuntimeException {
    public PriceChangedException(Long orderId, double oldPrice, double newPrice) {
        super("Price changed for order ID: " + orderId +
                ". Confirmed price: " + oldPrice + ", Current price: " + newPrice);
    }
}

