package com.aim.aimcodingtest.user.entity;

import com.aim.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false, unique = true, length = 20)
    String username;

    @Column(nullable = false, length = 20)
    String password;

    @Column(nullable = false, length = 20)
    String name;

    @Column(length = 100)
    String email;

    @Column(length = 15)
    String phoneNumber;

    @Column(length = 200)
    String address;
}
