package com.bidwave.bidwave_backend.controllers;

import com.bidwave.bidwave_backend.models.User;
import com.bidwave.bidwave_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController — handles HTTP requests and converts responses to JSON
// @RequestMapping — all endpoints start with /api/users
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    // UserRepository injected directly — no Service needed for simple lookups
    private final UserRepository userRepository;

    // handles GET /api/users/{id}
    // returns one user by their id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(user);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // handles GET /api/users/username/{username}
    // returns one user by their username
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseEntity.ok(user);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}