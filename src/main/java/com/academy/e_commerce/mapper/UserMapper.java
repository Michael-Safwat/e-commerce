package com.academy.e_commerce.mapper;

import com.academy.e_commerce.model.User;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.dto.UserDTO;

public class UserMapper {

    private UserMapper(){}

    public static UserDTO userToUserDTO(User user) {
        return new UserDTO(
                user.getEmail(),
                user.getName()
                );
    }

    public static User userRegistrationDTOToUser(UserRegistrationDTO userRegistrationDTO) {
        return User.builder()
                .email(userRegistrationDTO.email())
                .password(userRegistrationDTO.password())
                .name(userRegistrationDTO.name())
                .build();
    }

}
