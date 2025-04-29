package com.aim.aimcodingtest.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequest {
    String username;
    String password;
}
