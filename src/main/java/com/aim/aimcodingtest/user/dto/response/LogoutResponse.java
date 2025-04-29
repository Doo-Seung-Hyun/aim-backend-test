package com.aim.aimcodingtest.user.dto.response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class LogoutResponse {
    private String username;
    private LocalDateTime loggedOutAt;

    public static LogoutResponse of(String username) {
        return LogoutResponse.builder()
                .username(username)
                .loggedOutAt(LocalDateTime.now())
                .build();
    }
}
