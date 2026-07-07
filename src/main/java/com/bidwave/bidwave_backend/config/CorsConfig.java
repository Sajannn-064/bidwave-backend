package com.bidwave.bidwave_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration — tells Spring this class contains configuration
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")           // apply to all endpoints
                .allowedOrigins(
                    "http://localhost:5173", // React dev server
                    "https://bidwave.vercel.app" // production frontend on Vercel
                )
                .allowedMethods(
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE",
                    "OPTIONS"
                )
                .allowedHeaders("*")         // allow all headers including Authorization
                .allowCredentials(true);     // allow cookies and auth headers
    }
}