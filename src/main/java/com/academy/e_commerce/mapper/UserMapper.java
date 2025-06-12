package com.academy.e_commerce.mapper;

import com.academy.e_commerce.model.User;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.dto.UserDTO;

public class UserMapper {

    private UserMapper(){}
    public static UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User userRegistrationDTOToUser(UserRegistrationDTO userRegistrationDTO) {
        return User.builder()
                .email(userRegistrationDTO.getEmail())
                .password(userRegistrationDTO.getPassword())
                .name(userRegistrationDTO.getName())
                .build();
    }

}
