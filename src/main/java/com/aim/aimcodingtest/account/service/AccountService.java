package com.aim.aimcodingtest.account.service;

import com.aim.aimcodingtest.account.dto.response.DepositResponse;
import com.aim.aimcodingtest.account.dto.response.WithdrawResponse;
import com.aim.aimcodingtest.account.entity.Account;

public interface AccountService {
    Account findAccountForTransaction(String accountNumber);
    DepositResponse deposit(String accountNumber, Long amount);
    WithdrawResponse withdraw(String accountNumber, Long amount);
}
