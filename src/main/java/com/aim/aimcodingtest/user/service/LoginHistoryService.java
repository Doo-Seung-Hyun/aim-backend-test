package com.aim.aimcodingtest.user.service;

import com.aim.aimcodingtest.user.entity.LoginHistory;
import com.aim.aimcodingtest.user.entity.User;
import com.aim.aimcodingtest.user.enums.ErrorReason;
import com.aim.aimcodingtest.user.repository.LoginHistoryRepository;
import com.aim.aimcodingtest.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    public void recordLogin(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            log.error("로그인을 완료하여 기록하려 했으나 유저를 찾을 수 없습니다. username: " + username);
            return;
        }
        LoginHistory loginHistory = LoginHistory.ofLogin(user, getClientIp());
        loginHistoryRepository.save(loginHistory);
    }

    public void recordLogout(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            log.error("로그아웃을 완료하여 기록하려 했으나 유저를 찾을 수 없습니다. username: " + username);
            return;
        }
        LoginHistory loginHistory = LoginHistory.ofLogout(user, getClientIp());
        loginHistoryRepository.save(loginHistory);
    }

    public void recordLoginError(String username, AuthenticationException exception) {
        ErrorReason errorReason = exception instanceof UsernameNotFoundException ? ErrorReason.INVALID_USERNAME
                : exception instanceof BadCredentialsException ? ErrorReason.INVALID_PASSWORD
                : ErrorReason.INVALID_USERNAME;

        LoginHistory loginHistory = LoginHistory.ofLoginFail(username, getClientIp(), errorReason);
        loginHistoryRepository.save(loginHistory);
    }

    private String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 여러 IP가 있을 경우 첫 번째 IP 반환
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
}
