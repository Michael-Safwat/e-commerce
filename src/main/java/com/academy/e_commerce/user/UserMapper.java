package com.academy.e_commerce.user;

import com.academy.e_commerce.user.dto.UserRegistrationDTO;
import com.academy.e_commerce.user.dto.UserDTO;

public class UserMapper {

    public static UserDTO userToUserDTO(User user){
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User UserRegistrationDTOToUser(UserRegistrationDTO UserRegistrationDTO){
        return User.builder()
                .email(UserRegistrationDTO.getEmail())
                .password(UserRegistrationDTO.getPassword())
                .name(UserRegistrationDTO.getName())
                .build();
    }

}
