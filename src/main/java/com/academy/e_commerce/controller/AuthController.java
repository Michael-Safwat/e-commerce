package com.academy.e_commerce.controller;

import com.academy.e_commerce.advice.SuccessResponse;
import com.academy.e_commerce.config.security.jwt.Token;
import com.academy.e_commerce.model.Password;
import com.academy.e_commerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.endpoint.base-url}")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Token> getLoginInfo(@Valid Authentication authentication) {
        return new ResponseEntity<>(this.authService.createJwtToken(authentication), HttpStatus.OK);
    }

    @PostMapping("/reactivate")
    public ResponseEntity<SuccessResponse> sendReactivation(@RequestParam("email") String email) {
        authService.sendReactivationLink(email);
        return ResponseEntity.ok(new SuccessResponse("If an account with that email exists, a reactivation link has been sent."));
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody Password newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successful");
    }

}
