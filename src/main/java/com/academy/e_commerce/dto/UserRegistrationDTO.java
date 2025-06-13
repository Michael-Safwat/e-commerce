package com.academy.e_commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {

    @NotBlank(message = "email can't be empty")
    @Email(message = "must be valid email")
    private String email;

    @NotNull(message = "password can't be empty")
    private String password;

    @NotBlank(message = "name can't be empty")
    private String name;
}
