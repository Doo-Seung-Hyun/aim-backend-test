package com.aim.aimcodingtest.user.service;

import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import com.aim.aimcodingtest.user.dto.request.RegisterRequest;
import com.aim.aimcodingtest.user.dto.response.LoginResponse;
import com.aim.aimcodingtest.user.dto.response.LogoutResponse;
import com.aim.aimcodingtest.user.dto.response.RegisterResponse;
import com.aim.aimcodingtest.user.entity.User;
import com.aim.aimcodingtest.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest request;

    /**
     * 로그인
     */
    @Transactional(readOnly = true)
    public LoginResponse login(String username, String password) throws ServletException {
        User user;
        try {
            // 인증
            Authentication token = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(token);

            // 로그인 기록
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );

            // 로그인 유저정보 반환
            user = (User) authentication.getPrincipal();
        }catch (AuthenticationException e) {
            log.warn(String.format("로그인 실패 : %s", username));
            throw new ApiException(ErrorCode.LOGIN_FAILED);
        }

        LocalDateTime loggedInAt = LocalDateTime.now();

        return LoginResponse.fromUser(user, loggedInAt);
    }

    /**
     * 회원 가입
     */
    public RegisterResponse register(RegisterRequest registerRequest) {
        // ID 중복 검사
        String userName = registerRequest.getUsername();
        userRepository.findByUsername(userName).ifPresent(user->{
            throw new ApiException(ErrorCode.DUPLICATED_USERNAME);
        });

        User user = User.of(registerRequest);

        //패스워드 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 가입 진행
        user = userRepository.save(user);
        return RegisterResponse.builder()
                .username(user.getUsername())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
