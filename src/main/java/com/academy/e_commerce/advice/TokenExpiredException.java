package com.academy.e_commerce.advice;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Reset token has expired. Please request a new link.");
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
