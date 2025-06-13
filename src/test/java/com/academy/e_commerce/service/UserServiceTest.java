package com.academy.e_commerce.service;

import com.academy.e_commerce.config.security.UserSecurityService;
import com.academy.e_commerce.dto.UserDTO;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.mapper.UserMapper;
import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import com.academy.e_commerce.model.UserPrincipal;
import com.academy.e_commerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserSecurityService userSecurityService;

    @InjectMocks
    private UserService userService;

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

    private UserRegistrationDTO getSampleCustomerDTO() {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setEmail("customer@example.com");
        dto.setPassword("customer123");
        dto.setName("customer");
        return dto;
    }

    @Test
    void testLoadUserByUsername_found() {
        User user = getSampleAdminUser();
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

        UserDetails result = userSecurityService.loadUserByUsername("admin@example.com");

        assertNotNull(result);
        assertEquals("admin@example.com", result.getUsername());
        verify(userRepository).findByEmail("admin@example.com");
    }

    @Test
    void testLoadUserByUsername_notFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userSecurityService.loadUserByUsername("missing@example.com"));
    }
    

    @Test
    void testRegisterCustomer() {
        UserRegistrationDTO dto = getSampleCustomerDTO();

        when(passwordEncoder.encode("customer123")).thenReturn("hashed-customer123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        UserDTO result = userService.registerCustomer(dto);

        assertNotNull(result);
        assertEquals("customer@example.com", result.getEmail());
        verify(passwordEncoder).encode("customer123");
        verify(userRepository).save(any(User.class));
    }
}
