package com.academy.e_commerce.config.security.listener;

import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();

        Optional<User> userOpt = userRepository.findByEmail(email);
        try {
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                boolean userIsLocked = user.getIsLocked();
                if (!userIsLocked && user.getRoles().contains("ROLE_CUSTOMER")) {
                    user.setFailedAttempts(0);
                    userRepository.save(user);
                }
            }
        }
        catch (Exception e) {
            throw new LockedException("User is suspended check your mail to active your account.");

        }
    }
}