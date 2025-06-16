package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.UserDTO;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.mapper.UserMapper;
import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.model.VerificationToken;
import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public String registerUser(UserRegistrationDTO registrationDTO) {

        Optional<User> existingUserOptional = userRepository.findByEmail(registrationDTO.email());

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            if (Boolean.TRUE.equals(existingUser.getIsVerified())) {
                throw new RuntimeException("User already exists and is verified.");
            } else {
                resendVerification(existingUser);
                throw new RuntimeException("Verification email resent. Check your inbox.");
            }
        }

        User user = UserMapper.userRegistrationDTOToUser(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        user.setIsLocked(false);
        user.setRoles(Set.of(Role.CUSTOMER));
        user.setIsVerified(false);
        userRepository.save(user);


        return  generateAndSendVerificationToken(user);

    }

    private String generateAndSendVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        verificationTokenRepository.save(verificationToken);

//        emailService.sendVerificationEmail(user.getEmail(), token);
        return token;
    }

    private void resendVerification(User user) {
        verificationTokenRepository.findByUser(user).ifPresent(verificationTokenRepository::delete);

        generateAndSendVerificationToken(user);
    }

    public void verifyUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token."));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired.");
        }

        User user = verificationToken.getUser();
        user.setIsVerified(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken); // Clean up
    }
}
