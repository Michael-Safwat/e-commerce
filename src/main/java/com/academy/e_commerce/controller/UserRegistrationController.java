package com.academy.e_commerce.controller;

import com.academy.e_commerce.service.UserRegistrationService;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    public UserRegistrationController(UserRegistrationService userRegistrationService)
    {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegistrationDTO dto) {
        return ResponseEntity.ok( userRegistrationService.registerUser(dto));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        userRegistrationService.verifyUser(token);
        return ResponseEntity.ok("Account successfully verified.");
    }
}
