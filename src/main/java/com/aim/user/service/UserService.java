package com.aim.user.service;

import com.aim.common.exception.ApiException;
import com.aim.common.exception.ErrorCode;
import com.aim.user.dto.request.LoginRequest;
import com.aim.user.dto.response.RegisterResponse;
import com.aim.user.entity.User;
import com.aim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.aim.user.dto.response.RegisterRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public String login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        //username과 password로 user를 찾지 못하면 오류
        User user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(()->new ApiException(ErrorCode.USER_NOT_FOUND));

        // jwt 생성
        String token = "generated-token";
        return token;
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
