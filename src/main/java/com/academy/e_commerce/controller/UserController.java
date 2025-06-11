package com.academy.e_commerce.controller;

import com.academy.e_commerce.model.User;
import com.academy.e_commerce.service.UserService;
import com.academy.e_commerce.dto.CustomerRegistrationDTO;
import com.academy.e_commerce.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admins/register")
    public ResponseEntity<UserDTO> registerAdmin(@RequestBody User admin) {
        UserDTO savedAdmin = this.userService.registerAdmin(admin);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
    }

    @PostMapping("/customers/register")
    public ResponseEntity<UserDTO> registerCustomer(@RequestBody CustomerRegistrationDTO customer) {
        UserDTO savedCustomer = this.userService.registerCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
}
