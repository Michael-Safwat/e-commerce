package com.academy.e_commerce.admin;

import com.academy.e_commerce.admin.dto.AdminDTO;

public class AdminMapper {

    public static AdminDTO adminToAdminDTO(Admin admin) {
        return AdminDTO.builder()
                .id(admin.getId())
                .name(admin.getName())
                .username(admin.getUsername())
                .roles(admin.getRoles())
                .build();
    }
}
