package com.bidwave.bidwave_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO — carries only the fields a client is allowed to send during registration
// No @Entity — this is not a database table, just a data carrier
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    // only these three fields accepted from client
    // role and id are set by the server — never by the client
    private String username;
    private String email;
    private String password;
}