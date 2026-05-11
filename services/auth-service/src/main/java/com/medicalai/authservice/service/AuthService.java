package com.medicalai.authservice.service;

import com.medicalai.authservice.entity.User;
import com.medicalai.authservice.model.AuthResponse;
import com.medicalai.authservice.model.LoginRequest;
import com.medicalai.authservice.model.RegisterRequest;
import com.medicalai.authservice.repository.UserRepository;
import com.medicalai.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User.Role userRole = User.Role.USER;
        if (request.getRole() != null) {
            userRole = User.Role.valueOf(request.getRole().getValue());
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();
        
        userRepository.save(user);
        
        var jwtToken = jwtService.generateToken(user);
        
        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        response.setExpiresIn(jwtService.getExpirationTime());
        response.setRole(user.getRole().name());
        return response;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        
        var jwtToken = jwtService.generateToken(user);
        
        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        response.setExpiresIn(jwtService.getExpirationTime());
        response.setRole(user.getRole().name());
        return response;
    }
}
