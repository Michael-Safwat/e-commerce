package com.academy.e_commerce.controller;

import com.academy.e_commerce.dto.AdminDTO;
import com.academy.e_commerce.dto.UserDTO;
import com.academy.e_commerce.dto.UserRegistrationDTO;
import com.academy.e_commerce.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("${api.endpoint.base-url}/portal")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerAdmin(@RequestBody @Valid UserRegistrationDTO admin) {
        UserDTO savedAdmin = adminService.registerAdmin(admin);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<AdminDTO>> getAllAdmins(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return new ResponseEntity<>(adminService.getAllAdmins(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getAdminById(@PathVariable("id") Long id) {
        Optional<UserDTO> adminDto = adminService.getAdminById(id);
        return adminDto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateAdminById(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO){
        return new ResponseEntity<>(this.adminService.updateAdminById(id, userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminById(@PathVariable("id") Long id){
        return new ResponseEntity<>(this.adminService.deleteAdminById(id), HttpStatus.NO_CONTENT);
    }

}
