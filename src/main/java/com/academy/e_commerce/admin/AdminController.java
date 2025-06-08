package com.academy.e_commerce.admin;

import com.academy.e_commerce.admin.dto.AdminDTO;
import com.academy.e_commerce.system.Result;
import com.academy.e_commerce.system.StatusCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public Result registerAdmin(@RequestBody Admin admin){
        AdminDTO savedAdmin = this.adminService.registerAdmin(admin);
        return new Result(true, StatusCode.CREATED, "New Admin registered",savedAdmin);
    }
}
