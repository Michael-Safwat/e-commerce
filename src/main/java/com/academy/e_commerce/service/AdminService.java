package com.academy.e_commerce.service;

import com.academy.e_commerce.advice.SuperAdminDeletionException;
import com.academy.e_commerce.dto.UserDTO;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.mapper.UserMapper;
import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = UserMapper.userRegistrationDTOToUser(admin);
        user.setPassword(passwordEncoder.encode(admin.password()));
        user.setIsVerified(false);
        user.setIsLocked(false);
        user.setRoles(Set.of(Role.ADMIN));
        User savedAdmin = userRepository.save(user);
        return UserMapper.userToUserDTO(savedAdmin);
    }
    public Page<UserDTO> getAllAdmins(Pageable pageable) {
        Page<User> admins = userRepository.findAll(pageable);
        return admins.map(UserMapper::userToUserDTO);
    }

    public Optional<UserDTO> getAdminById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::userToUserDTO);
    }

    public UserDTO updateAdminById(Long id, UserDTO userDTO) {

        User user = this.userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No such user found"));

        user.setEmail(userDTO.email());
        user.setName(userDTO.name());
        return UserMapper.userToUserDTO(this.userRepository.save(user));
    }

    public Void deleteAdminById(Long id) {

        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent() && user.get().getRoles().contains(Role.SUPER_ADMIN))
            throw new SuperAdminDeletionException("Cannot delete super admin account: ");
        this.userRepository.deleteById(id);
        return null;
    }
}
