package com.bidwave.bidwave_backend.repositories;

import com.bidwave.bidwave_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// @Repository marks this as a Spring Bean — Spring manages it and enables exception translation
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring auto-generates: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Spring auto-generates: SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);
}