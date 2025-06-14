package com.academy.e_commerce.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class SuperAdminDeletionException extends RuntimeException {

    public SuperAdminDeletionException(String message) {
        super(message);
    }
}
