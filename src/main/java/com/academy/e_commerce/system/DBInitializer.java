package com.academy.e_commerce.system;

import com.academy.e_commerce.admin.Admin;
import com.academy.e_commerce.admin.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


//todo: rename file with adminInitializer
@Component
public class DBInitializer implements CommandLineRunner {

    private final AdminService adminService;

    public DBInitializer(AdminService adminService) {
        this.adminService = adminService;
    }

    //todo: use a logger for checking
    @Override
    public void run(String... args) throws Exception {

        Admin admin = Admin.builder()
                .name("Michael")
                .username("mike")
                .password("123")
                .roles("super_admin")
                .isEnabled(true)
                .isLocked(false)
                .build();

        this.adminService.registerAdmin(admin);
    }
}
