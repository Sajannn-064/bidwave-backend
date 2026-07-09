package com.bidwave.bidwave_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

// @Component — marks this as a Spring Bean so it can be injected/registered
// @RequiredArgsConstructor — Lombok generates the constructor for final fields (jwtUtil)
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements ChannelInterceptor {

    // injected — reuses the same JwtUtil already used by JwtAuthFilter
    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // wrap raw message into STOMP-aware accessor — gives access to command + headers
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // only check identity on the initial CONNECT frame — not every message after
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // read custom Authorization header sent by client on CONNECT
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                // strip "Bearer " prefix to get raw token string
                String token = authHeader.substring(7);

                // extract username from token payload
                String username = jwtUtil.extractUsername(token);

                // validate signature + expiry + username match
                if (jwtUtil.validateToken(token, username)) {

                    // build Principal — credentials null (token already trusted), no authorities yet
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, null);

                    // attach identity to this STOMP session — persists for the whole connection
                    accessor.setUser(authentication);

                } else {
                    // invalid/expired token — reject the handshake
                    throw new RuntimeException("Invalid or expired token");
                }

            } else {
                // no Authorization header present — reject the handshake
                throw new RuntimeException("Missing Authorization header on CONNECT");
            }
        }

        return message;
    }
}