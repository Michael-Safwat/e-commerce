package com.academy.e_commerce.repository;

import com.academy.e_commerce.model.Role;
import com.academy.e_commerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    Page<User> findByRolesIn(List<Role> roles, Pageable pageable);

}
