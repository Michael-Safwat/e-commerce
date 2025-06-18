package com.academy.e_commerce.advice;

public class VerificationException extends RuntimeException {
    public VerificationException(String message) {
        super(message);
    }
}