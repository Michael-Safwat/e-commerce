package com.academy.e_commerce.controller;

import com.academy.e_commerce.service.UserRegistrationService;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.dto.UserDTO;
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

//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> register(@RequestBody UserRegistrationDTO dto) {
//        UserDTO userDTO = userRegistrationService.registerUser(dto);
//        return ResponseEntity.ok(userDTO);
//    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDTO dto) {
        String userVerificationToken = userRegistrationService.registerUser(dto);
        return ResponseEntity.ok(userVerificationToken);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        userRegistrationService.verifyUser(token);
        return ResponseEntity.ok("Account successfully verified.");
    }
}
