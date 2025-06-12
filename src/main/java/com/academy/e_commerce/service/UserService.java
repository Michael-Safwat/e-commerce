package com.academy.e_commerce.service;

import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.mapper.UserMapper;
import com.academy.e_commerce.model.UserPrincipal;
import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.dto.CustomerRegistrationDTO;
import com.academy.e_commerce.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(email)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("username " + email + " is not found."));
    }

    public UserDTO registerAdmin(User admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        User savedAdmin = this.userRepository.save(admin);
        return UserMapper.userToUserDTO(savedAdmin);
    }


    public UserDTO registerCustomer(CustomerRegistrationDTO customer) {

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        User user = UserMapper.customerRegistrationDTOToUser(customer);
        user.setIsEnabled(false);
        user.setIsLocked(false);
        user.setRoles(Set.of(Role.CUSTOMER));
        User savedCustomer = this.userRepository.save(user);
        return UserMapper.userToUserDTO(savedCustomer);
    }
}
