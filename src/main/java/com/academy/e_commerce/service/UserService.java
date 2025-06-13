package com.academy.e_commerce.service;

import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.mapper.UserMapper;
import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserDTO registerCustomer(UserRegistrationDTO customer) {

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        User user = UserMapper.userRegistrationDTOToUser(customer);
        user.setIsEnabled(false);
        user.setIsLocked(false);
        user.setRoles(Set.of(Role.CUSTOMER));
        User savedCustomer = this.userRepository.save(user);
        return UserMapper.userToUserDTO(savedCustomer);
    }
}
