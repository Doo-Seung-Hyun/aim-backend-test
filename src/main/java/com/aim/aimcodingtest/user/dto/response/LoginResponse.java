package com.aim.aimcodingtest.user.dto.response;

import com.aim.aimcodingtest.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class LoginResponse {
    String username;
    String name;
    String email;
    List<String> roles;
    String phoneNumber;
    LocalDateTime loggedInAt;

    public static LoginResponse fromUser(User user, LocalDateTime loggedInAt) {
        return LoginResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .phoneNumber(user.getPhoneNumber())
                .loggedInAt(loggedInAt)
                .build();
    }
}
