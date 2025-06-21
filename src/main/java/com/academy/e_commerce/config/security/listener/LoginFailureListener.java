package com.academy.e_commerce.config.security.listener;

import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.service.AuthService;
import com.academy.e_commerce.service.UserRegistrationService;
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
    private final AuthService authService;
    private final UserRegistrationService userRegistrationService;

    @Override
    @Transactional
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = (String) event.getAuthentication().getPrincipal();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();

        if (Boolean.FALSE.equals(user.getIsVerified())) {
            userRegistrationService.resendVerification(user);
            return;
        }

        int failed = user.getFailedAttempts() + 1;
        user.setFailedAttempts(failed);

        if (failed >= 3) {
            user.setIsLocked(true);
            userRepository.save(user);
//            authService.sendReactivationLink(email);
            return;
        }

        userRepository.save(user);
    }
}
