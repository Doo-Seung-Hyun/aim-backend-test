package com.aim.aimcodingtest.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorReason {
    INVALID_AMOUNT,
    INSUFFICIENT_BALANCE,
    NOT_AUTHORIZED,
    ETC_FAILED, INVALID_ACCOUNT;
}
