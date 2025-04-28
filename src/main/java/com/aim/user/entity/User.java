package com.aim.user.entity;

import com.aim.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User extends BaseEntity {
    @Column(nullable = false, unique = true, length = 20)
    String username;

    @Column(nullable = false, length = 20)
    String password;

    @Column(nullable = false, length = 20)
    String name;

    @Column(length = 100)
    String email;


    String phoneNumber;
}
