package com.bidwave.bidwave_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // handshake-level interceptor — validates JWT before the socket upgrades
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    // builds the Principal from the validated token, attaches it to the session
    private final JwtHandshakeHandler jwtHandshakeHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(
                    "http://localhost:5173",
                    "https://bidwave.vercel.app",
                    "http://127.0.0.1:5500",
                    "http://localhost:5500"
                )
                // run our JWT check during the HTTP handshake
                .addInterceptors(jwtHandshakeInterceptor)
                // use our custom handler to actually attach the Principal to the session
                .setHandshakeHandler(jwtHandshakeHandler)
                .withSockJS();
    }

    // configureClientInboundChannel is no longer needed — the old
    // ChannelInterceptor-based approach is fully replaced by the two
    // components above, which authenticate at the HTTP handshake instead
}