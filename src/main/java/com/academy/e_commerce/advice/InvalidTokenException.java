package com.academy.e_commerce.advice;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Invalid reset token.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
