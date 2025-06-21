package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.UserDTO;
import com.academy.e_commerce.mapper.UserMapper;
import com.academy.e_commerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/user")
public class CustomerController {

    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("authentication.principal.claims['userId'] == #userId")
    public ResponseEntity<UserDTO> getProfile(@PathVariable("userId")Long userId){
        return ResponseEntity.ok(UserMapper.userToUserDTO(userService.getUserById(userId)));
    }
}
