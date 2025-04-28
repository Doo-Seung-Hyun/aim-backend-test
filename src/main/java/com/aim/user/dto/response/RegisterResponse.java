package com.aim.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class RegisterResponse {
    String username;
    LocalDateTime createdAt;
}
