package com.aim.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequest {
    String username;
    String password;
}
