package com.aim.aimcodingtest.account.service;

import com.aim.aimcodingtest.account.dto.response.DepositResponse;
import com.aim.aimcodingtest.account.dto.response.WithdrawResponse;
import com.aim.aimcodingtest.account.entity.Account;
import com.aim.aimcodingtest.account.entity.Transaction;
import com.aim.aimcodingtest.account.enums.TransactionType;
import com.aim.aimcodingtest.account.repository.TransactionRepository;
import com.aim.aimcodingtest.common.exception.ApiException;
import com.aim.aimcodingtest.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public abstract class AbstractTransactionLoggingService implements AccountService {
    private final TransactionRepository transactionRepository;

    @Override
    public DepositResponse deposit(String accountNumber, Long amount) {
        Account account = null;
        Transaction transaction = null;
        DepositResponse temporaryResponse = null;
        try {
            // 트랜잭션 생성
            transaction = Transaction.createDepositTransaction(accountNumber, amount);

            // 계좌 찾기
            account = findAccountForTransaction(accountNumber);

            // 입금 실행
            temporaryResponse = doDeposit(account, amount, transaction.getTransactionTime());
        } catch (Exception e) {
            log.warn(e.getMessage());
            //트랜잭션 오류 기록
            transactionRepository.save(transaction.error(account, e));
            throw e;
        }
        // 트랜잭션 정상 기록
        transactionRepository.save(transaction.success(account));

        return temporaryResponse.complete(transaction.getTransactionId(),
                transaction.getTransactionTime());
    }

    @Override
    public WithdrawResponse withdraw(String accountNumber, Long amount) {
        Account account = null;
        Transaction transaction = null;
        WithdrawResponse temporaryResponse = null;
        try {
            // 트랜잭션 생성
            transaction = Transaction.createDepositTransaction(accountNumber, amount);

            // 계좌찾기
            account = findAccountForTransaction(accountNumber);

            // 출금 실행
            temporaryResponse = doWithdraw(account, amount, transaction.getTransactionTime());
        } catch (Exception e) {
            log.warn(e.getMessage());
            //트랜잭션 오류 기록
            transactionRepository.save(transaction.error(account, e));
            throw e;
        }
        return temporaryResponse.complete(transaction.getTransactionId(),
                transaction.getTransactionTime());
    }

    //입금로직 구현필요
    protected abstract DepositResponse doDeposit(Account account, Long amount, LocalDateTime transactionTime);

    //출금로직 구현필요
    protected abstract WithdrawResponse doWithdraw(Account account, Long amount, LocalDateTime transactionTime);

}
