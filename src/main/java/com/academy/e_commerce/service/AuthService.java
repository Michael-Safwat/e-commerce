package com.academy.e_commerce.service;

import com.academy.e_commerce.config.security.jwt.JwtProvider;
import com.academy.e_commerce.config.security.jwt.Token;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtProvider jwtProvider,
                       UserRepository userRepository,
                       EmailService emailService,
                       PasswordEncoder passwordEncoder)
    {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public Token createJwtToken(Authentication authentication) {
        return new Token(this.jwtProvider.createToken(authentication));
    }

    @Transactional
    public String sendReactivationLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getIsLocked()) {
            throw new IllegalStateException("Account is not locked.");
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetExpiryDate(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

//        emailService.sendReactivationEmail(email, token);
        return token;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (user.getResetExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setIsLocked(false);
        user.setFailedAttempts(0);
        user.setResetToken(null);
        user.setResetExpiryDate(null);
        userRepository.save(user);
    }
}
