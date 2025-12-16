package com.whyboutrasseyt.miniproject.presentation;

import com.whyboutrasseyt.miniproject.application.command.LoginCommand;
import com.whyboutrasseyt.miniproject.application.dto.UserDto;
import com.whyboutrasseyt.miniproject.application.service.AuthService;
import com.whyboutrasseyt.miniproject.presentation.request.LoginRequest;
import com.whyboutrasseyt.miniproject.presentation.response.LoginResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(new LoginCommand(request.email(), request.password()));
        UserDto user = authService.currentUser(token);
        return ResponseEntity.ok(new LoginResponse(token, user));
    }
}
