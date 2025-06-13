package com.academy.e_commerce.service;

import com.academy.e_commerce.config.security.jwt.JwtProvider;
import com.academy.e_commerce.config.security.jwt.Token;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    public AuthService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public Token createJwtToken(Authentication authentication) {
        return new Token(this.jwtProvider.createToken(authentication));
    }
}
