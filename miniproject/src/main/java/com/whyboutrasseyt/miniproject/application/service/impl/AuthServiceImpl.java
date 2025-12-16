package com.whyboutrasseyt.miniproject.application.service.impl;

import com.whyboutrasseyt.miniproject.application.command.LoginCommand;
import com.whyboutrasseyt.miniproject.application.dto.UserDto;
import com.whyboutrasseyt.miniproject.application.service.AuthService;
import com.whyboutrasseyt.miniproject.domain.exception.ValidationException;
import com.whyboutrasseyt.miniproject.domain.model.User;
import com.whyboutrasseyt.miniproject.domain.repository.UserRepository;
import com.whyboutrasseyt.miniproject.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String login(LoginCommand command) {
        validate(command);
        String email = command.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Invalid credentials"));
        if (!passwordEncoder.matches(command.password(), user.passwordHash())) {
            throw new ValidationException("Invalid credentials");
        }
        return jwtService.generateToken(user);
    }

    @Override
    public UserDto currentUser(String token) {
        if (token == null || token.isBlank()) {
            throw new ValidationException("Missing token");
        }
        String email;
        try {
            email = jwtService.extractEmail(token);
        } catch (Exception ex) {
            throw new ValidationException("Invalid token");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Invalid token"));
        return new UserDto(user.id(), user.email(), user.role());
    }

    private void validate(LoginCommand command) {
        if (command == null) {
            throw new ValidationException("Command is required");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (command.password() == null || command.password().isBlank()) {
            throw new ValidationException("Password is required");
        }
    }

}
