package com.academy.e_commerce.service;

import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.UserRepository;
import com.academy.e_commerce.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRegistrationServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private UserRegistrationService userRegistrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserRegistrationDTO getSampleCustomerDTO() {
        return new UserRegistrationDTO("customer@example.com", "customer123", "customer");
    }

    @Test
    void testRegisterUser_newUser_successful() {
        UserRegistrationDTO dto = getSampleCustomerDTO();

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.password())).thenReturn("encoded123");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        String result = userRegistrationService.registerUser(dto);

        assertEquals("User Registration Successful!", result);

        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals("customer@example.com", savedUser.getEmail());
        assertEquals("encoded123", savedUser.getPassword());
        assertEquals(Set.of(Role.CUSTOMER), savedUser.getRoles());
        assertFalse(savedUser.getIsVerified());
    }
}
