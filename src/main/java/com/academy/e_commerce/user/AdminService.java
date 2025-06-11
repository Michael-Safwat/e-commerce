package com.academy.e_commerce.user;

import com.academy.e_commerce.user.dto.UserDTO;
import com.academy.e_commerce.user.dto.UserRegistrationDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AdminService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }



    public UserDTO registerAdmin(UserRegistrationDTO admin){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("admin");
        User user = UserMapper.UserRegistrationDTOToUser(admin);
        User savedAdmin = this.userRepository.save(user);
        return UserMapper.userToUserDTO(savedAdmin);
    }

    public List<UserDTO> getAllAdmins() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().contains("admin"))
                .map(UserMapper::userToUserDTO)
                .collect(Collectors.toList());
    }




}
