package com.aim.aimcodingtest.user.controller;

import com.aim.aimcodingtest.common.dto.response.ApiResponse;
import com.aim.aimcodingtest.user.dto.request.LoginRequest;
import com.aim.aimcodingtest.user.dto.request.RegisterRequest;
import com.aim.aimcodingtest.user.dto.response.LoginResponse;
import com.aim.aimcodingtest.user.dto.response.LogoutResponse;
import com.aim.aimcodingtest.user.dto.response.RegisterResponse;
import com.aim.aimcodingtest.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<LoginResponse> response = ApiResponse.success(
                userService.login(loginRequest)
        );
        return response.toResponseEntity();
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        ApiResponse<RegisterResponse> response = ApiResponse.success(
                userService.register(registerRequest)
        );
        return response.toResponseEntity();
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(HttpServletRequest request) throws ServletException {
        ApiResponse<LogoutResponse> response = ApiResponse.success(
                userService.logout(request)
        );
        return response.toResponseEntity();
    }
}
