package com.academy.e_commerce.security;

import com.academy.e_commerce.admin.Admin;
import com.academy.e_commerce.admin.AdminMapper;
import com.academy.e_commerce.admin.dto.AdminDTO;
import com.academy.e_commerce.customer.Customer;
import com.academy.e_commerce.customer.CustomerMapper;
import com.academy.e_commerce.customer.dto.CustomerDTO;
import com.academy.e_commerce.user.User;
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

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        //create user info
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        //create JWT
        Map<String, Object> loginResultMap = new HashMap<>();

        if (user.getRoles().equals("customer")) {
            CustomerDTO customerDTO = CustomerMapper.customerToCustomerDTO((Customer) user);
            String token = this.jwtProvider.createToken(authentication);

            loginResultMap.put("customerInfo", customerDTO);
            loginResultMap.put("token", token);

        } else if (user.getRoles().equals("admin")|| user.getRoles().equals("super_admin")) {
            AdminDTO adminDTO = AdminMapper.adminToAdminDTO((Admin) user);
            String token = this.jwtProvider.createToken(authentication);

            loginResultMap.put("adminInfo", adminDTO);
            loginResultMap.put("token", token);
        }

        return loginResultMap;
    }
}
