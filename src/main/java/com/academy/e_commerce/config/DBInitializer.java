package com.academy.e_commerce.config;

import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.ProductRepository;
import com.academy.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DBInitializer implements CommandLineRunner {

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPlainPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;


    public DBInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User admin = User.builder()
                .name(adminName)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPlainPassword))
                .roles(Set.of(Role.SUPER_ADMIN))
                .isVerified(true)
                .isLocked(false)
                .failedAttempts(0)
                .build();


        this.userRepository.save(admin);

        Product product = Product.builder()
                .name("Wireless Mouse")
                .description("Ergonomic wireless mouse with adjustable DPI")
                .stock(100)
                .price(29.99)
                .category("Electronics")
                .image("https://example.com/images/wireless-mouse.jpg")
                .rating(5.0)
                .build();

        productRepository.save(product);

    }


}
