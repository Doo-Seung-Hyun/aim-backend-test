package com.aim.aimcodingtest.user.service;

import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import com.aim.aimcodingtest.user.dto.request.LoginRequest;
import com.aim.aimcodingtest.user.dto.request.RegisterRequest;
import com.aim.aimcodingtest.user.dto.response.LoginResponse;
import com.aim.aimcodingtest.user.dto.response.RegisterResponse;
import com.aim.aimcodingtest.user.entity.User;
import com.aim.aimcodingtest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        //username과 password로 user를 찾지 못하면 오류
        User user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(()->new ApiException(ErrorCode.USER_NOT_FOUND));

        LocalDateTime loggedInAt = LocalDateTime.now();
        //todo : LoginHistory 구현

        return LoginResponse.fromUser(user, loggedInAt);
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        // 사용자 ID 중복 검사
        String userName = registerRequest.getUsername();
        userRepository.findByUsername(userName).ifPresent(user->{
            throw new ApiException(ErrorCode.DUPLICATED_USERNAME);
        });

        // 가입 진행
        User user = userRepository.save(registerRequest.toUser());
        return RegisterResponse.builder()
                .username(user.getUsername())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
