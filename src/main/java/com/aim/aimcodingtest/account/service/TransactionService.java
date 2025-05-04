package com.aim.aimcodingtest.account.service;

import com.aim.aimcodingtest.account.entity.Account;
import com.aim.aimcodingtest.account.entity.Transaction;
import com.aim.aimcodingtest.account.enums.TransactionType;
import com.aim.aimcodingtest.account.repository.TransactionRepository;
import com.aim.aimcodingtest.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

}
