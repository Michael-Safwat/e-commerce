package com.academy.e_commerce.config;

import com.academy.e_commerce.model.Product;
import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.ProductRepository;
import com.academy.e_commerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;


    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User admin = User.builder()
                .name("Michael")
                .email("mike@example.com")
                .password("123")
                .roles(Set.of(Role.SUPER_ADMIN))
                .isVerified(true)
                .isLocked(false)
                .build();

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

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
