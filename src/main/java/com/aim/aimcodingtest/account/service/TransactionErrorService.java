package com.aim.aimcodingtest.account.service;

import com.aim.aimcodingtest.account.entity.Transaction;
import com.aim.aimcodingtest.account.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionErrorService {
    private final TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveErrorTransaction(Transaction transaction) {
        transactionRepository.saveAndFlush(transaction);
        log.info("거래오류가 발생했습니다 Transaction-ID: {}", transaction.getTransactionId());
    }
}
