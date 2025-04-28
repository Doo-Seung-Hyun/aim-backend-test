package com.aim.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.aim.user.dto.request.LoginRequest;
import com.aim.user.dto.response.RegisterRequest;
import com.aim.user.service.UserService;

@RestController("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String token = userService.login(loginRequest);
        return "generated-token";
    }

    @PostMapping("/signup")
    public void register(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
    }
}
