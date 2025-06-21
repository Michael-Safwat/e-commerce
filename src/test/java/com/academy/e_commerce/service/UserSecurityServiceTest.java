package com.academy.e_commerce.service;

import com.academy.e_commerce.config.security.UserSecurityService;
import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserSecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRegistrationService userRegistrationService;

    @InjectMocks
    private UserSecurityService userSecurityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User getSampleAdminUser() {
        User user = new User();
        user.setEmail("admin@example.com");
        user.setPassword("plain-password");
        user.setRoles(Set.of(Role.ADMIN));
        return user;
    }

    @Test
    void testLoadUserByUsername_found() {
        User user = getSampleAdminUser();
        user.setIsVerified(true);
        user.setIsLocked(false);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

        UserDetails result = userSecurityService.loadUserByUsername("admin@example.com");

        assertNotNull(result);
        assertEquals("admin@example.com", result.getUsername());
        verify(userRepository).findByEmail("admin@example.com");
    }

    @Test
    void testLoadUserByUsername_userNotVerified_throwsDisabledException() {
        User user = getSampleAdminUser();
        user.setIsVerified(false);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
        doNothing().when(userRegistrationService).resendVerification(user);

        DisabledException exception = assertThrows(DisabledException.class,
                () -> userSecurityService.loadUserByUsername("admin@example.com"));

        assertEquals("User account is not verified", exception.getMessage());
        verify(userRepository).findByEmail("admin@example.com");
        verify(userRegistrationService).resendVerification(user);
    }

    @Test
    void testLoadUserByUsername_userLocked_throwsLockedException() {
        User user = getSampleAdminUser();
        user.setIsVerified(true);
        user.setIsLocked(true);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

        LockedException exception = assertThrows(LockedException.class,
                () -> userSecurityService.loadUserByUsername("admin@example.com"));

        assertEquals("User account is locked", exception.getMessage());
        verify(userRepository).findByEmail("admin@example.com");
    }

    @Test
    void testLoadUserByUsername_userNotFound_throwsUsernameNotFoundException() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userSecurityService.loadUserByUsername("admin@example.com"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail("admin@example.com");
    }
}
