package com.academy.e_commerce.user;

import com.academy.e_commerce.system.Result;
import com.academy.e_commerce.system.StatusCode;
import com.academy.e_commerce.user.dto.CustomerRegistrationDTO;
import com.academy.e_commerce.user.dto.UserDTO;
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
    public Result registerAdmin(@RequestBody User admin){
        UserDTO savedAdmin = this.userService.registerAdmin(admin);
        return new Result(true, StatusCode.CREATED, "New Admin registered", savedAdmin);
    }

    @PostMapping("/customers/register")
    public Result registerCustomer(@RequestBody CustomerRegistrationDTO customer){
        UserDTO savedCustomer = this.userService.registerCustomer(customer);
        return new Result(true, StatusCode.CREATED, "New Customer registered", savedCustomer);
    }
}
