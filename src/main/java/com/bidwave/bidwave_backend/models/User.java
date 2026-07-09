package com.bidwave.bidwave_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lombok: generates getters, setters, equals, hashCode, toString
@Data
// Lombok: generates empty constructor — required by JPA to reconstruct objects from DB rows
@NoArgsConstructor
// Lombok: generates all-fields constructor — used when creating a User manually in code
@AllArgsConstructor
// JPA: marks this class as a database entity
@Entity
// JPA: maps this class to the "users" table in PostgreSQL
@Table(name = "users")
public class User {

    // Primary key — PostgreSQL auto-increments this
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username must be unique and cannot be null
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    // email must be unique and cannot be null
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // stores BCrypt hashed password — never plain text
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    // role defines access level e.g. USER, ADMIN
    @Column(name = "role", nullable = false)
    private String role;
}