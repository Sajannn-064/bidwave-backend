package com.bidwave.bidwave_backend.config;

import com.bidwave.bidwave_backend.models.User;
import com.bidwave.bidwave_backend.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

// @Component — marks this as a Spring Bean
// OncePerRequestFilter — guarantees this filter runs exactly once per request
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // JwtUtil and UserRepository injected via constructor injection
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // read Authorization header from incoming request
        // valid header looks like: "Bearer eyJhbGci..."
        String authHeader = request.getHeader("Authorization");

        // if no Authorization header or doesn't start with Bearer — skip JWT check
        // request continues but will be blocked by SecurityConfig if route is protected
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract token by removing "Bearer " prefix (7 characters)
        String token = authHeader.substring(7);

        // extract username from token payload
        String username = jwtUtil.extractUsername(token);

        // if username extracted and no authentication set yet in this request
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // fetch real user from database using extracted username
            User user = userRepository.findByUsername(username).orElse(null);

            // validate token against real user from database
            if (user != null && jwtUtil.validateToken(token, user.getUsername())) {

                // create authentication object with user's role
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null, // no password needed here — token already validated
                                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                        );

                // set authentication in Spring Security context
                // this tells Spring Security — this request is authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // pass request to next filter or Controller
        filterChain.doFilter(request, response);
    }
}