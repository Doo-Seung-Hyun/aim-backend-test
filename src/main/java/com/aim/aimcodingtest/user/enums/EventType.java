package com.aim.aimcodingtest.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
    LOGIN("로그인"),
    LOGOUT("로그아웃"),
    LOGIN_FAIL("로그인 실패");

    private final String description;
}
