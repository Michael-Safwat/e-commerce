package com.academy.e_commerce.system;

import com.academy.e_commerce.user.User;
import com.academy.e_commerce.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer implements CommandLineRunner {

    private final UserService userService;

    public DBInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        User admin = User.builder()
                .name("Michael")
                .email("mike@example.com")
                .password("123")
                .roles("super_admin")
                .isEnabled(true)
                .isLocked(false)
                .build();

        this.userService.registerAdmin(admin);
    }
}
