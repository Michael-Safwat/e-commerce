package com.academy.e_commerce.user;

import com.academy.e_commerce.user.dto.UserRegistrationDTO;
import com.academy.e_commerce.user.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


    public UserDTO registerCustomer(UserRegistrationDTO customer) {

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        User user = UserMapper.UserRegistrationDTOToUser(customer);
        user.setIsEnabled(false);
        user.setIsLocked(false);
        user.setRole("customer");
        User savedCustomer = this.userRepository.save(user);
        return UserMapper.userToUserDTO(savedCustomer);
    }
}
