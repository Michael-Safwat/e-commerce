package com.academy.e_commerce.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "abstract_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//todo : add trials to suspend depending on it
//todo : add email attribute
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;//todo : rename this with userName
    private String password;
    private String name;
    private String roles;
    //Should the trials be in admin attributes?
    private Boolean isLocked; //todo : as business required replace this with status
    private Boolean isEnabled; //todo: remove this when you add status
}
