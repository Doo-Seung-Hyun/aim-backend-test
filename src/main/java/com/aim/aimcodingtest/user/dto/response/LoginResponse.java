package com.aim.aimcodingtest.user.dto.response;

import com.aim.aimcodingtest.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class LoginResponse {
    String username;
    String name;
    String email;
    String role;
    String phoneNumber;
    LocalDateTime loggedInAt;

    public static LoginResponse fromUser(User user, LocalDateTime loggedInAt) {
        return LoginResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .loggedInAt(loggedInAt)
                .build();
    }
}
