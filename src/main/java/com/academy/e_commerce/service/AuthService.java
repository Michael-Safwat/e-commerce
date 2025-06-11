package com.academy.e_commerce.service;

import com.academy.e_commerce.config.security.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    public AuthService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public Map<String, String> createJwtToken(Authentication authentication) {

        Map<String, String> loginResultMap = new HashMap<>();
        String token = this.jwtProvider.createToken(authentication);
        loginResultMap.put("token", token);
        return loginResultMap;
    }
}
