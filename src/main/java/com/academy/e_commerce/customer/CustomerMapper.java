package com.academy.e_commerce.customer;

import com.academy.e_commerce.customer.dto.CustomerDTO;

public class CustomerMapper {

    public static CustomerDTO customerToCustomerDTO(Customer customer){
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .username(customer.getUsername())
                .roles(customer.getRoles())
                .build();
    }
}
