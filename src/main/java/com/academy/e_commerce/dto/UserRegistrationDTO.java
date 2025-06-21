package com.academy.e_commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRegistrationDTO(

        @NotBlank(message = "email can't be empty")
        @Email(message = "must be valid email")
        @NotBlank(message = "Email is required")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Invalid email format"
        )
        String email,

        @NotNull(message = "password can't be empty")
        String password,

        @NotBlank(message = "name can't be empty")
        String name) {
}
