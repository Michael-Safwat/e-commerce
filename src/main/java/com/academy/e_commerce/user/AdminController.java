package com.academy.e_commerce.user;

import com.academy.e_commerce.system.Result;
import com.academy.e_commerce.system.StatusCode;
import com.academy.e_commerce.user.dto.UserDTO;
import com.academy.e_commerce.user.dto.UserRegistrationDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("/register")
    public Result registerAdmin(@RequestBody UserRegistrationDTO admin){
        UserDTO savedAdmin = this.adminService.registerAdmin(admin);
        return new Result(true, StatusCode.CREATED, "New Admin registered", savedAdmin);
    }

    @GetMapping("")
    public List<UserDTO> getAllAdmins()
    {
        return adminService.getAllAdmins();
    }
}
