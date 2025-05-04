package com.aim.aimcodingtest.user.entity;

import com.aim.aimcodingtest.common.entity.BaseEntity;
import com.aim.aimcodingtest.user.enums.ErrorReason;
import com.aim.aimcodingtest.user.enums.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginHistory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(nullable = false)
    private LocalDateTime eventTime;

    @Column(nullable = false, length = 15)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ErrorReason errorReason;

    public static LoginHistory ofLogin(User user, String ipAddress) {
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUser(user);
        loginHistory.setUsername(user.getUsername());
        loginHistory.setEventType(EventType.LOGIN);
        loginHistory.setEventTime(LocalDateTime.now());
        loginHistory.setIpAddress(ipAddress);
        return loginHistory;
    }

    public static LoginHistory ofLogout(User user, String ipAddress) {
        LoginHistory loginHistory = ofLogin(user, ipAddress);
        loginHistory.setEventType(EventType.LOGOUT);
        return loginHistory;
    }

    public static LoginHistory ofLoginFail(String username, String ipAddress, ErrorReason errorReason) {
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUsername(username);
        loginHistory.setEventType(EventType.LOGIN_FAIL);
        loginHistory.setEventTime(LocalDateTime.now());
        loginHistory.setIpAddress(ipAddress);
        loginHistory.setErrorReason(errorReason);
        return loginHistory;
    }
}
