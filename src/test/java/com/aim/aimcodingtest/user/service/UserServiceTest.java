package com.aim.aimcodingtest.user.service;

import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.user.dto.request.LoginRequest;
import com.aim.aimcodingtest.user.dto.request.RegisterRequest;
import com.aim.aimcodingtest.user.dto.response.LoginResponse;
import com.aim.aimcodingtest.user.dto.response.RegisterResponse;
import com.aim.aimcodingtest.user.entity.User;
import com.aim.aimcodingtest.user.entity.UserBuilder;
import com.aim.aimcodingtest.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    @Mock
    private PasswordEncoder encoder;

    LoginRequest loginRequest;
    RegisterRequest registerRequest;
    User user;
    @BeforeEach
    void setUp() {
        user = UserBuilder.builder().build();
        loginRequest = LoginRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        registerRequest = RegisterRequest.builder()
                .username("test-user")
                .password("test-password")
                .name("홍길동")
                .build();
    }

    @Test
    void 로그인_실패(){
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(mock(AuthenticationException.class));

        ApiException e = assertThrows(ApiException.class, () ->
                userService.login("wrong-username", "wrong-password"));
        System.out.println(e.getMessage());
    }

    @Test
    void 로그인시_정상_처리() throws ServletException {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        HttpSession session = new MockHttpSession();

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(request.getSession(true)).thenReturn(session);
        when(authentication.getPrincipal()).thenReturn(user);

        LoginResponse response = userService.login(username, password);
        assertNotNull(response);
        assertEquals(username, response.getUsername());
    }

    @Test
    void 회원가입시_username이_중복인경우_오류(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mock(User.class)));
        ApiException e = assertThrows(ApiException.class, () -> userService.register(registerRequest));
        System.out.println(e.getMessage());
    }

    @Test
    void 회원가입_정상(){
        String password = "encoded-password";
        User user = UserBuilder.builder().password(password).build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(encoder.encode(anyString())).thenReturn(password);
        when(userRepository.save(any(User.class))).thenReturn(user);

        RegisterResponse response = userService.register(registerRequest);
        assertNotNull(response);
        assertEquals(user.getUsername(), response.getUsername());
    }
}