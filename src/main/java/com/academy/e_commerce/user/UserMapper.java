package com.academy.e_commerce.user;

import com.academy.e_commerce.user.dto.CustomerRegistrationDTO;
import com.academy.e_commerce.user.dto.UserDTO;

public class UserMapper {

    public static UserDTO userToUserDTO(User user){
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User customerRegistrationDTOToUser(CustomerRegistrationDTO customerRegistrationDTO){
        return User.builder()
                .email(customerRegistrationDTO.getEmail())
                .password(customerRegistrationDTO.getPassword())
                .name(customerRegistrationDTO.getName())
                .build();
    }

}
