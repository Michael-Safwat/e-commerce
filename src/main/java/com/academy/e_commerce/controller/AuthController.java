package com.academy.e_commerce.controller;

import com.academy.e_commerce.config.security.jwt.Token;
import com.academy.e_commerce.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api.endpoint.base-url}")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Token> getLoginInfo(Authentication authentication) {
        return new ResponseEntity<>(this.authService.createJwtToken(authentication), HttpStatus.OK);
    }
}
