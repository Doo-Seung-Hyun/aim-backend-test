package com.aim.aimcodingtest.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request -> request
                        .requestMatchers("/api/users/login", "/api/users/register","/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrfConfig->csrfConfig.disable())
                .formLogin(formLoginConfig->formLoginConfig.disable())
                .httpBasic(httpBasicConfig->httpBasicConfig.disable())
                .logout(logoutConfig->logoutConfig.disable())
                .headers(headers -> headers.frameOptions(config->config.sameOrigin()))
                .sessionManagement(sessionManagementConfig->
                        sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .build();
    }
}
