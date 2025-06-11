package com.academy.e_commerce.system;

import com.academy.e_commerce.user.AdminService;
import com.academy.e_commerce.user.User;
import com.academy.e_commerce.user.UserMapper;
import com.academy.e_commerce.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final AdminService adminService;

    public AdminInitializer(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) throws Exception {

        User admin = User.builder()
                .name("Michael")
                .email("mike@example.com")
                .password("123")
                .role("super_admin")
                .isEnabled(true)
                .isLocked(false)
                .build();

        this.adminService.registerAdmin(admin);
    }
}
