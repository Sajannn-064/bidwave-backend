package com.bidwave.bidwave_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// @Configuration — tells Spring this class contains configuration
// @EnableWebSocketMessageBroker — enables WebSocket with STOMP messaging
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // enable simple in-memory message broker
        // clients subscribe to topics starting with /topic
        registry.enableSimpleBroker("/topic");

        // prefix for messages sent FROM client TO server
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // WebSocket connection endpoint
        // clients connect to ws://localhost:8080/ws
        registry.addEndpoint("/ws")
                .setAllowedOrigins(
                    "http://localhost:5173",
                    "https://bidwave.vercel.app"
                )
                // SockJS fallback — if WebSocket not supported, uses HTTP long polling
                .withSockJS();
    }
}