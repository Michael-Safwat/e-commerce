package com.academy.e_commerce.config;

import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AdminInitializer(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        User admin = User.builder()
                .name("Michael")
                .email("mike@example.com")
                .password("123")
                .roles(Set.of(Role.SUPER_ADMIN))
                .isEnabled(true)
                .isLocked(false)
                .build();

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        this.userRepository.save(admin);
    }
}
