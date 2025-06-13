package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.UserDTO;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.mapper.UserMapper;
import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerAdmin(UserRegistrationDTO admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        User user = UserMapper.userRegistrationDTOToUser(admin);
        user.setIsVerified(false);
        user.setIsLocked(false);
        user.setRoles(Set.of(Role.ADMIN));
        User savedAdmin = userRepository.save(user);
        return UserMapper.userToUserDTO(savedAdmin);
    }
    public List<UserDTO> getAllAdmins() {
        List<User> admins = userRepository.findAll();
        return admins.stream().map(UserMapper::userToUserDTO).toList();
    }

    public Optional<UserDTO> getAdminById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::userToUserDTO);
    }



}
