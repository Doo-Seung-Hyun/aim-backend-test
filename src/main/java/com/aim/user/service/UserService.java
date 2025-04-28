package com.aim.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.aim.user.dto.response.RegisterRequest;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String login(String username, String password) {
        return "generated-token";
    }

    public void register(RegisterRequest registerRequest) {
        String userName = registerRequest.getUsername();
        userRepository.findByUsername(userName);
    }
}
