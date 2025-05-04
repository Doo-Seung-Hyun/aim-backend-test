package com.aim.aimcodingtest.common.listener;

import com.aim.aimcodingtest.user.service.LoginHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler extends HttpStatusReturningLogoutSuccessHandler {
    private final LoginHistoryService loginHistoryService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = authentication.getName();
        super.onLogoutSuccess(request, response, authentication);
        loginHistoryService.recordLogout(username);
    }
}
