package com.academy.e_commerce.config.security.listener;

import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = (String) event.getAuthentication().getPrincipal();
        Optional<User> userOpt = userRepository.findByEmail(email);
        userOpt.ifPresent(user -> {
            int failed = user.getFailedAttempts() + 1;
            user.setFailedAttempts(failed);
            if (failed >= 3) {
                user.setIsLocked(true);
            }
            userRepository.save(user);
        });
    }
}
