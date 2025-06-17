package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.UserDTO;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.advice.RegistrationException;
import com.academy.e_commerce.advice.VerificationException;
import com.academy.e_commerce.mapper.UserMapper;
import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.model.VerificationToken;
import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(registrationDTO.email());

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (Boolean.TRUE.equals(existingUser.getIsVerified())) {
                log.warn("User {} already registered and verified.", existingUser.getEmail());
                throw new RegistrationException("User already exists.");
            }

            log.info("User {} exists but not verified; resending token.", existingUser.getEmail());
            resendVerification(existingUser);
            throw new RegistrationException("Verification email resent. Check your inbox.");
        }

        // Create new user if not exists
        return createNewUser(registrationDTO);
    }

    private UserDTO createNewUser(UserRegistrationDTO registrationDTO) {
        User user = UserMapper.userRegistrationDTOToUser(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        user.setIsLocked(false);
        user.setRoles(Set.of(Role.CUSTOMER));
        user.setIsVerified(false);

        userRepository.save(user);
        log.info("Created new user with id {} and email {}", user.getId(), user.getEmail());

        generateAndSendVerificationToken(user);
        return UserMapper.userToUserDTO(user);
    }

    private void generateAndSendVerificationToken(User user) {
        // delete old token if exists (optional safety)
        verificationTokenRepository.findByUser(user).ifPresent(verificationTokenRepository::delete);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(5));

        verificationTokenRepository.save(verificationToken);
        log.info("Generated verification token for user {}: {}", user.getEmail(), token);

        try {
            emailService.sendVerificationEmail(user.getEmail(), user.getName(), token);
            log.info("Verification email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to {}", user.getEmail(), e);
            throw new RegistrationException("User registered but failed to send verification email. Please try again later.");
        }
    }

    public void resendVerification(User user) {
        if (Boolean.TRUE.equals(user.getIsVerified())) {
            log.info("User {} is already verified, no need to resend token.", user.getEmail());
            throw new RegistrationException("User already verified.");
        }

        generateAndSendVerificationToken(user);
    }

    public void verifyUser(String token) {
        log.info("Verifying user with token {}", token);

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Invalid verification token attempted: {}", token);
                    return new VerificationException("Invalid verification token.");
                });

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Verification token expired for user {}", verificationToken.getUser().getEmail());
            throw new VerificationException("Token expired.");
        }

        User user = verificationToken.getUser();
        user.setIsVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);

        log.info("User {} successfully verified", user.getEmail());
    }
}
