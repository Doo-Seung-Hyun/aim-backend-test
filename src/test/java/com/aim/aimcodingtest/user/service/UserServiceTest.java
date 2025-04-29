package com.aim.aimcodingtest.user.service;

import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.user.dto.request.LoginRequest;
import com.aim.aimcodingtest.user.dto.request.RegisterRequest;
import com.aim.aimcodingtest.user.entity.User;
import com.aim.aimcodingtest.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    LoginRequest loginRequest;
    RegisterRequest registerRequest;
    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .username("test-user")
                .password("test-password")
                .build();
        registerRequest = RegisterRequest.builder()
                .username("test-user")
                .password("test-password")
                .name("홍길동")
                .build();
    }

    @Test
    void 로그인시_ID와_PW로_사용자를_찾지_못하는_경우_오류(){
        when(userRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.empty());

        ApiException e = assertThrows(ApiException.class, () -> userService.login(loginRequest));
        System.out.println(e.getMessage());
    }

    @Test
    void 로그인시_정상_처리(){
        User user = registerRequest.toUser();
        when(userRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.login(loginRequest));
    }

    @Test
    void 회원가입시_username이_중복인경우_오류(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mock(User.class)));
        ApiException e = assertThrows(ApiException.class, () -> userService.register(registerRequest));
        System.out.println(e.getMessage());
    }

    @Test
    void 회원가입_정상(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(registerRequest.toUser());
        assertDoesNotThrow(() -> userService.register(registerRequest));
    }
}