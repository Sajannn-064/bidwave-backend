package com.bidwave.bidwave_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// @Configuration — tells Spring this class contains Bean definitions
// @EnableWebSecurity — enables Spring Security for the application
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // JwtAuthFilter injected — added to the filter chain
    private final JwtAuthFilter jwtAuthFilter;

    // defines the security filter chain — the rules for every request
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // enable CORS using the bean defined below — required for browser-based
            // cross-origin requests (including SockJS's /ws/info handshake ping)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // disable CSRF — not needed for stateless JWT APIs
            .csrf(csrf -> csrf.disable())

            // define which routes are public and which are protected
            .authorizeHttpRequests(auth -> auth

                // public routes — no token needed
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auctions/active",
                    "/api/auctions/{id}",
                    "/api/bids/auction/{id}",

                    // WebSocket handshake + SockJS info/polling endpoints —
                    // these are plain HTTP requests that hit Spring Security
                    // before the STOMP CONNECT frame is ever reached, so they
                    // must be permitted here separately from JwtHandshakeInterceptor
                    "/ws/**"
                ).permitAll()

                // all other routes require authentication
                .anyRequest().authenticated()
            )

            // stateless session — no server side sessions, JWT handles auth
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // add JwtAuthFilter before Spring's default auth filter
            .addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS configuration — tells the browser which origins may call this API
    // separate from WebSocketConfig's setAllowedOrigins, which only governs
    // the WebSocket upgrade itself, not plain HTTP requests like /ws/info
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://bidwave.vercel.app",
            "http://127.0.0.1:5500",
            "http://localhost:5500"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // BCryptPasswordEncoder Bean — used by AuthService to hash passwords
    // defined here so Spring can inject it wherever PasswordEncoder is needed
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}