package com.academy.e_commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegistrationDTO(

        @NotBlank(message = "email can't be empty")
        @Email(message = "must be valid email")
        String email,

        @NotNull(message = "password can't be empty")
        String password,

        @NotBlank(message = "name can't be empty")
        String name) {
}
