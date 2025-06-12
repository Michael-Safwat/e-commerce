package com.academy.e_commerce.config;

import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserService userService;

    public AdminInitializer(UserService userService) {
        this.userService = userService;
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

        this.userService.registerAdmin(admin);
    }
}
