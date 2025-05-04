package com.aim.aimcodingtest.account.service;

import com.aim.aimcodingtest.account.dto.response.AccountResponse;
import com.aim.aimcodingtest.account.dto.response.DepositResponse;
import com.aim.aimcodingtest.account.dto.response.WithdrawResponse;
import com.aim.aimcodingtest.account.entity.Account;
import com.aim.aimcodingtest.account.repository.AccountRepository;
import com.aim.aimcodingtest.account.repository.TransactionRepository;
import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AccountServiceImpl extends AbstractTransactionLoggingService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        super(transactionRepository);
        this.accountRepository = accountRepository;
    }


    @Transactional(readOnly = true)
    public AccountResponse findAccount(Long id) throws ApiException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_ACCOUNT_ID));

        if (!validateAccountOwner(account))
            throw new ApiException(ErrorCode.NOT_AUTHENTICATED);

        return AccountResponse.fromAccount(account);
    }

    @Transactional(readOnly = true)
    public AccountResponse findAccount(String accountNumber) throws ApiException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_ACCOUNT_ID));

        if (!validateAccountOwner(account))
            throw new ApiException(ErrorCode.NOT_AUTHENTICATED);

        return AccountResponse.fromAccount(account);
    }

    @Override
    public Account findAccountForTransaction(String accountNumber) throws ApiException {
        Account account = accountRepository.findByAccountNumberWithLock(accountNumber)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_ACCOUNT_NUMBER));

        if (!validateAccountOwner(account))
            throw new ApiException(ErrorCode.NOT_AUTHENTICATED);

        return account;
    }

    @Override
    protected DepositResponse doDeposit(Account account, Long amount, LocalDateTime transactionTime) throws ApiException {
        account.deposit(amount, transactionTime);
        return DepositResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balanceAfterDeposit(account.getBalance())
                .depositAmount(amount)
                .build();
    }

    @Override
    protected WithdrawResponse doWithdraw(Account account, Long amount, LocalDateTime transactionTime) throws ApiException {
        account.withdraw(amount, transactionTime);
        return WithdrawResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balanceAfterWithdraw(account.getBalance())
                .withdrawAmount(amount)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<AccountResponse> getAllAccountsByUsername(String username, Pageable pageable) {
        Page<Account> accounts = accountRepository.findByUser_username(username, pageable);
        return accounts.map(AccountResponse::fromAccount);
    }

    private boolean validateAccountOwner(Account account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
            || !authentication.isAuthenticated()
            || !account.getUser().getUsername().equals(authentication.getName()))
            return false;

        return true;
    }

}
