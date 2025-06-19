package com.academy.e_commerce.advice;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
