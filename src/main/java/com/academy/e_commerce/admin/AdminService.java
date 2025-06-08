package com.academy.e_commerce.admin;

import com.academy.e_commerce.admin.dto.AdminDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminDTO registerAdmin(Admin admin){

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Admin savedAdmin = this.adminRepository.save(admin);
        return AdminMapper.adminToAdminDTO(savedAdmin);
    }
}
