package com.medicalai.authservice.controller;

import com.medicalai.authservice.api.AuthApi;
import com.medicalai.authservice.model.AuthResponse;
import com.medicalai.authservice.model.ErrorResponse;
import com.medicalai.authservice.model.LoginRequest;
import com.medicalai.authservice.model.RegisterRequest;
import com.medicalai.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthResponse> registerUser(RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(registerRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage(e.getMessage());
            error.setTimestamp(LocalDateTime.now().toString());
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<AuthResponse> loginUser(LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid credentials");
            error.setTimestamp(LocalDateTime.now().toString());
            return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
        }
    }
}
