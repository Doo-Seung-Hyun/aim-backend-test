package com.aim.aimcodingtest.account.controller;

import com.aim.aimcodingtest.account.dto.request.DepositRequest;
import com.aim.aimcodingtest.account.dto.request.WithdrawRequest;
import com.aim.aimcodingtest.account.dto.response.AccountResponse;
import com.aim.aimcodingtest.account.dto.response.DepositResponse;
import com.aim.aimcodingtest.account.dto.response.WithdrawResponse;
import com.aim.aimcodingtest.account.entity.Account;
import com.aim.aimcodingtest.account.entity.AccountBuilder;
import com.aim.aimcodingtest.account.service.AccountServiceImpl;
import com.aim.aimcodingtest.user.entity.User;
import com.aim.aimcodingtest.user.entity.UserBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountServiceImpl accountService;

    Account account = AccountBuilder.builder().build();

    @Test
    @WithMockUser
    void 계좌조회_정상() throws Exception {
        AccountResponse response = AccountResponse.fromAccount(account);
        when(accountService.findAccount(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 로그인하지_않은_경우_계좌조회_실패() throws Exception {
        AccountResponse response = AccountResponse.fromAccount(account);
        when(accountService.findAccount(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "test-user")
    void 나의_계좌조회_정상() throws Exception {
        Account anotherAccount = AccountBuilder.builder()
                .id(2L).accountNumber("987654321").balance(20000L)
                .build();
        Page<AccountResponse> responses = new PageImpl<>(List.of(
                AccountResponse.fromAccount(account),
                AccountResponse.fromAccount(anotherAccount)
        ));
        when(accountService.getAllAccountsByUsername(anyString(), any(Pageable.class)))
                .thenReturn(responses);

        User user = UserBuilder.builder().username("test-user").build();
        mockMvc.perform(get("/api/accounts/my")
                        .with(SecurityMockMvcRequestPostProcessors.user(user)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void 입금처리_정상() throws Exception {
        DepositRequest request = DepositRequest.builder()
                .accountNumber("123456789")
                .amount(10000L)
                .build();

        DepositResponse response = DepositResponse.builder()
                .accountNumber(request.getAccountNumber())
                .depositAmount(request.getAmount())
                .balanceAfterDeposit(20000L)
                .build();

        when(accountService.deposit(anyString(), anyLong())).thenReturn(response);

        mockMvc.perform(post("/api/accounts/deposit")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 로그인하지_않은_경우_입금처리_실패() throws Exception {
        DepositRequest request = DepositRequest.builder()
                .accountNumber("123456789")
                .amount(10000L)
                .build();

        mockMvc.perform(post("/api/accounts/deposit")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void 출금처리_정상() throws Exception {
        WithdrawRequest request = WithdrawRequest.builder()
                .accountNumber("123456789")
                .amount(10000L)
                .build();

        WithdrawResponse response = WithdrawResponse.builder()
                .accountNumber(request.getAccountNumber())
                .withdrawAmount(request.getAmount())
                .balanceAfterWithdraw(20000L)
                .build();

        when(accountService.withdraw(anyString(), anyLong())).thenReturn(response);

        mockMvc.perform(post("/api/accounts/withdraw")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 로그인하지_않은_경우_출금처리_실패() throws Exception {
        WithdrawRequest request = WithdrawRequest.builder()
                .accountNumber("123456789")
                .amount(10000L).build();

        mockMvc.perform(post("/api/accounts/withdraw")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}