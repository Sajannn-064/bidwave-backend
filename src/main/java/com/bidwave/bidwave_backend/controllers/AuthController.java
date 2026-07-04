package com.bidwave.bidwave_backend.controllers;

import com.bidwave.bidwave_backend.dto.LoginRequest;
import com.bidwave.bidwave_backend.dto.RegisterRequest;
import com.bidwave.bidwave_backend.models.User;
import com.bidwave.bidwave_backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

// @RestController — handles HTTP requests and converts responses to JSON
// @RequestMapping — all endpoints start with /api/auth
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Spring injects AuthService automatically via constructor injection
    private final AuthService authService;

    // handles POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // pass DTO fields directly to AuthService
            User user = authService.register(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword()
            );

            // build success response
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", user.getUsername());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // handles POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // pass DTO fields directly to AuthService
            User user = authService.login(
                    request.getEmail(),
                    request.getPassword()
            );

            // JWT token will be added here in config layer
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", user.getUsername());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
} 