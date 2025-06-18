package com.academy.e_commerce.service;


import com.academy.e_commerce.model.User;
import com.academy.e_commerce.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById (Long userId){
        return this.userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not found"));
    }
}
