package com.bidwave.bidwave_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO — carries only the fields a client is allowed to send during login
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    // login only needs email and password
    private String email;
    private String password;
}