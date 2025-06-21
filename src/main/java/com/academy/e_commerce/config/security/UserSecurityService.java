package com.academy.e_commerce.config.security;

import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.service.UserRegistrationService;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRegistrationService  userRegistrationService;

    public UserSecurityService(UserRepository userRepository,UserRegistrationService userRegistrationService) {
        this.userRepository = userRepository;
        this.userRegistrationService = userRegistrationService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> {
                    if (Boolean.FALSE.equals(user.getIsVerified())) {
                        userRegistrationService.resendVerification(user);

                        throw new DisabledException("User account is not verified");
                    }
                    if (Boolean.TRUE.equals(user.getIsLocked())) {
                        throw new LockedException("User account is locked");
                    }
                    return new UserPrincipal(user); // implements UserDetails
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
