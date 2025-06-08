package com.academy.e_commerce.admin.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {

    private Long id;

    private String username;
    private String name;
    private String roles;
}
