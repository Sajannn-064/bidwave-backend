package com.bidwave.bidwave_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

// @Component — marks this as a Spring Bean so it can be injected/registered
// @RequiredArgsConstructor — Lombok generates the constructor for final fields (jwtUtil)
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    // injected — reuses the same JwtUtil already used by JwtAuthFilter
    private final JwtUtil jwtUtil;

    // runs once, during the plain HTTP handshake, before the socket upgrades
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                    WebSocketHandler wsHandler, Map<String, Object> attributes) {

        // browsers can't send custom headers on a raw WebSocket handshake,
        // so the token travels as a query parameter instead: ws://.../ws?token=xxx
        String query = request.getURI().getQuery();

        if (query != null && query.contains("token=")) {

            // pull the raw token value out of the query string
            String token = query.substring(query.indexOf("token=") + 6);

            // extract username from token payload
            String username = jwtUtil.extractUsername(token);

            // validate signature + expiry + username match
            if (jwtUtil.validateToken(token, username)) {

                // store username in session attributes — read later by JwtHandshakeHandler
                attributes.put("username", username);
                return true; // allow handshake to continue
            }
        }

        // no valid token — reject the handshake entirely
        return false;
    }

    // runs after handshake completes — nothing needed here
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                WebSocketHandler wsHandler, Exception exception) {
    }
}