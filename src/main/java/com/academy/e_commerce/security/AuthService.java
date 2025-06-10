package com.academy.e_commerce.security;

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

    public Map<String, Object> createJwtToken(Authentication authentication) {

        Map<String, Object> loginResultMap = new HashMap<>();
        String token = this.jwtProvider.createToken(authentication);
        loginResultMap.put("token", token);
        return loginResultMap;
    }
}
