package com.aim.aimcodingtest.user.service;

import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import com.aim.aimcodingtest.user.dto.request.LoginRequest;
import com.aim.aimcodingtest.user.dto.request.RegisterRequest;
import com.aim.aimcodingtest.user.dto.response.LoginResponse;
import com.aim.aimcodingtest.user.dto.response.LogoutResponse;
import com.aim.aimcodingtest.user.dto.response.RegisterResponse;
import com.aim.aimcodingtest.user.entity.User;
import com.aim.aimcodingtest.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user;
        try {
            //username으로 user를 찾지 못하면 오류
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username : " + username + "을 찾을 수 없습니다."));

            //패스워드 일치하지 않으면 오류
            if (!passwordEncoder.matches(password, user.getPassword()))
                throw new BadCredentialsException("Password 가 일치하지 않습니다.");
        }catch (UsernameNotFoundException | BadCredentialsException e) {
            log.warn(String.format("로그인 실패 : %s", username));
            throw new ApiException(ErrorCode.LOGIN_FAILED);
        }

        //인증 처리
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                user.getUsername()
                , null
                , List.of(new SimpleGrantedAuthority(user.getRole().name()))
        ));

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

    public LogoutResponse logout(HttpServletRequest request) throws ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated())
            throw new ApiException(ErrorCode.NOT_AUTHENTICATED);

        String username = auth.getName();
        request.logout();
        return LogoutResponse.of(username);
    }
}
