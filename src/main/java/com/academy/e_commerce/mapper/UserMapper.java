package com.academy.e_commerce.mapper;

import com.academy.e_commerce.model.User;
import com.academy.e_commerce.dto.CustomerRegistrationDTO;
import com.academy.e_commerce.dto.UserDTO;

public class UserMapper {

    private UserMapper(){}

    public static UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User customerRegistrationDTOToUser(CustomerRegistrationDTO customerRegistrationDTO) {
        return User.builder()
                .email(customerRegistrationDTO.getEmail())
                .password(customerRegistrationDTO.getPassword())
                .name(customerRegistrationDTO.getName())
                .build();
    }

}
