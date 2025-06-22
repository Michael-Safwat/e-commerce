package com.academy.e_commerce.advice;


public class PasswordAlreadyUsedException extends RuntimeException {
    public PasswordAlreadyUsedException() {
        super("New password must be different from the old password.");
    }

    public PasswordAlreadyUsedException(String message) {
        super(message);
    }
}
