package com.aim.aimcodingtest.user.controller;

import com.aim.aimcodingtest.common.config.SecurityConfig;
import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import com.aim.aimcodingtest.common.listener.CustomLogoutHandler;
import com.aim.aimcodingtest.user.dto.request.LoginRequest;
import com.aim.aimcodingtest.user.dto.request.RegisterRequest;
import com.aim.aimcodingtest.user.dto.response.LoginResponse;
import com.aim.aimcodingtest.user.dto.response.RegisterResponse;
import com.aim.aimcodingtest.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class})
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    UserService userService;

    @MockitoBean
    private CustomLogoutHandler customLogoutHandler;

    LoginRequest loginRequest;
    RegisterRequest registerRequest;

    String validUsername;
    String invalidUsername;
    String validPassword;
    String invalidPassword;
    String validEmail;
    String invalidEmail;
    String name;

    @BeforeEach
    void setUp() {
        validUsername = "valid-username";
        invalidUsername = "ab1";
        validPassword = "valid-password1!";
        invalidPassword = "invalidpassword1";
        name = "홍길동";
        validEmail = "doosh17@naver.com";
        invalidEmail = "doosh17";

        loginRequest = LoginRequest.builder().username("test-user").password("test-password").build();
        registerRequest = RegisterRequest.builder()
                .username(validUsername)
                .password(validPassword)
                .name(name)
                .email(validEmail)
                .build();
    }

    @Test
    void 로그인_정상() throws Exception {
        when(userService.login(anyString(), anyString())).thenReturn(mock(LoginResponse.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 로그인_실패() throws Exception {
        when(userService.login(anyString(), anyString())).thenThrow(new ApiException(ErrorCode.LOGIN_FAILED));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 회원가입_정상() throws Exception {
        when(userService.register(any(RegisterRequest.class))).thenReturn(mock(RegisterResponse.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원가입_validation_실패() throws Exception {
        registerRequest = RegisterRequest.builder()
                .username(invalidUsername)
                .password(invalidPassword)
                .name("")
                .email(invalidEmail)
                .build();

        when(userService.register(any(RegisterRequest.class))).thenReturn(mock(RegisterResponse.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest))
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }
}