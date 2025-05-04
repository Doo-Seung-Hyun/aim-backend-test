package com.aim.aimcodingtest.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum ErrorReason {
    INVALID_USERNAME,
    INVALID_PASSWORD,
    ETC_FAILED;
}
