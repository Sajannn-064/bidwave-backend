package com.bidwave.bidwave_backend.services;

import com.bidwave.bidwave_backend.models.User;
import com.bidwave.bidwave_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service marks this as a Spring Bean in the Service layer
// @RequiredArgsConstructor — Lombok generates a constructor for all final fields
// Spring uses that constructor to inject dependencies automatically
@Service
@RequiredArgsConstructor
public class AuthService {

    // final fields — Spring injects these automatically via constructor injection
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // configured in SecurityConfig later

    @Transactional
    public User register(String username, String email, String password) {

        // check if email is already registered
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // check if username is already taken
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        // never store plain text password — hash it with BCrypt
        String hashedPassword = passwordEncoder.encode(password);

        // create new User object with role USER by default
        User user = new User(null, username, email, hashedPassword, "USER");

        // save to database via Repository and return saved User
        return userRepository.save(user);
    }

    public User login(String email, String password) {

        // find user by email — throw exception if not found
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // compare plain text password against stored BCrypt hash
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}