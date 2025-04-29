package com.aim.aimcodingtest.user.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }
}
