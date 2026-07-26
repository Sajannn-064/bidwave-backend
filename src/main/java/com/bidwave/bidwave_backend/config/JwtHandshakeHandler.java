package com.bidwave.bidwave_backend.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

// runs right after beforeHandshake — this is where the Principal is
// actually attached to the WebSocket session, not just a passing message
@Component
public class JwtHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) {

        // read the username we stored earlier in JwtHandshakeInterceptor
        String username = (String) attributes.get("username");

        // build Principal from the verified username — this becomes the session's identity
        return new UsernamePasswordAuthenticationToken(username, null, null);
    }
}