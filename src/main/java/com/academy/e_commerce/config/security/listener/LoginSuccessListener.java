package com.academy.e_commerce.config.security.listener;

import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
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
        userOpt.ifPresent(user -> {
            user.setFailedAttempts(0);
            userRepository.save(user);
        });
    }
}