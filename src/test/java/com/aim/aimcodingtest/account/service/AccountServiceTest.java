package com.aim.aimcodingtest.account.service;

import com.aim.aimcodingtest.account.dto.response.AccountResponse;
import com.aim.aimcodingtest.account.dto.response.DepositResponse;
import com.aim.aimcodingtest.account.entity.Account;
import com.aim.aimcodingtest.account.entity.AccountBuilder;
import com.aim.aimcodingtest.account.entity.Transaction;
import com.aim.aimcodingtest.account.repository.AccountRepository;
import com.aim.aimcodingtest.account.repository.TransactionRepository;
import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private Account account = AccountBuilder.builder().build();

    @BeforeEach
    void setUp() {
        account.getUser().setUsername("test-user");
        SecurityContextHolder.clearContext();
    }

    // 로그인 모킹 메서드
    private void setAuthenticate(String username) {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(authentication.isAuthenticated()).thenReturn(true);
    }

    @Nested()
    class 계좌조회_테스트 {


        @Test
        void 정상_계좌조회(){
            setAuthenticate("test-user");
            when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

            AccountResponse accountResponse = accountService.findAccount(account.getId());
            assertNotNull(accountResponse);
            assertEquals(accountResponse.getAccountNumber(), account.getAccountNumber());
        }

        @Test
        void 없는_계좌번호로_계좌조회를_하는_경우_오류(){
            when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

            ApiException e = assertThrows(ApiException.class, ()->accountService.findAccount(1l));
            assertEquals(ErrorCode.INVALID_ACCOUNT_ID, e.getErrorCode());
        }

        @Test
        void 로그인_하지않은_사용자가_계좌조회_하는_경우_오류(){
            when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

            ApiException e = assertThrows(ApiException.class, ()->accountService.findAccount(1l));
            assertEquals(ErrorCode.NOT_AUTHENTICATED, e.getErrorCode());
        }

        @Test
        void 계좌가_없는_사용자가_자신의_계좌조회를_하는_경우_빈_리스트_리턴(){
            Pageable pageable = PageRequest.of(0, 10);
            Page<Account> accounts = new PageImpl<>(List.of(), pageable, 0);

            when(accountRepository.findByUser_username(anyString(), isA(Pageable.class)))
                    .thenReturn(accounts);

            Page<AccountResponse> accountResponse = accountService.getAllAccountsByUsername(
                    "test-user", pageable
            );
            assertNotNull(accountResponse);
            assertEquals(0, accountResponse.getTotalElements());
            assertTrue(accountResponse.getContent().isEmpty());
        }
    }

    @Nested
    class 입출금_테스트{

        @BeforeEach
        void setUp() {
            when(transactionRepository.save(any(Transaction.class))).thenReturn(mock(Transaction.class));
        }

        @Test
        void 정상_입금(){
            long balanceBefore = account.getBalance();
            long amount = 1000l;
            setAuthenticate("test-user");

            when(accountRepository.findByAccountNumberWithLock(anyString()))
                    .thenReturn(Optional.of(account));
            DepositResponse depositResponse = accountService.deposit(account.getAccountNumber(), amount);
            assertNotNull(depositResponse);
            assertEquals(account.getAccountNumber(), depositResponse.getAccountNumber());
            assertEquals(account.getBalance(), balanceBefore + amount);
        }

        @Test
        void 입금계좌번호가_잘못된_경우_오류(){
            long amount = 1000l;
            String accountNumber = "wrong-account-number";
            when(accountRepository.findByAccountNumberWithLock(anyString()))
                    .thenReturn(Optional.empty());
            ApiException e = assertThrows(ApiException.class,
                    ()->accountService.deposit(accountNumber, amount));
            assertEquals(ErrorCode.INVALID_ACCOUNT_NUMBER, e.getErrorCode());
        }

        @Test
        void 입금액이_0원_이하인_경우_오류(){
            long amount = 0L;
            setAuthenticate("test-user");

            when(accountRepository.findByAccountNumberWithLock(anyString()))
                    .thenReturn(Optional.of(account));
            ApiException e = assertThrows(ApiException.class,
                    ()->accountService.deposit(account.getAccountNumber(), amount));
            assertEquals(ErrorCode.INVALID_TRANSACTION_AMOUNT, e.getErrorCode());
        }

        @Test
        void 출금계좌가_본인계좌가_아닌_경우_오류(){
            String accountNumber = "1234567890";
            long amount = 10000L;

            account = AccountBuilder.builder().build();
            account.getUser().setUsername("test-user");
            when(accountRepository.findByAccountNumberWithLock(anyString()))
                    .thenReturn(Optional.of(account));

            ApiException e = assertThrows(ApiException.class,
                    ()->accountService.withdraw(accountNumber, amount));
            assertEquals(ErrorCode.NOT_AUTHENTICATED, e.getErrorCode());
        }

        @Test
        @WithMockUser(username = "test-user")
        void 출금시_잔액이_부족한_경우_오류(){
            String accountNumber = "1234567890";
            long amount = 10000L;
            setAuthenticate("test-user");

            account = AccountBuilder.builder().balance(amount-1000L).build();
            when(accountRepository.findByAccountNumberWithLock(anyString()))
                    .thenReturn(Optional.of(account));
            ApiException e = assertThrows(ApiException.class,
                    ()->accountService.withdraw(accountNumber, amount));
            assertEquals(ErrorCode.INSUFFICIENT_BALANCE, e.getErrorCode());
        }

        @Test
        void 출금액이_0원_이하인_경우_오류(){
            String accountNumber = "1234567890";
            long amount = 0L;
            setAuthenticate("test-user");

            when(accountRepository.findByAccountNumberWithLock(anyString()))
                    .thenReturn(Optional.of(account));
            ApiException e = assertThrows(ApiException.class,
                    ()->accountService.withdraw(accountNumber, amount));
            assertEquals(ErrorCode.INVALID_TRANSACTION_AMOUNT, e.getErrorCode());
        }
    }
}