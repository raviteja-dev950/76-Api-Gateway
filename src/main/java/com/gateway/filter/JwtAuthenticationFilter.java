package com.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Allow auth endpoints without token - Public
        if (path.contains("/auth") || path.contains("/login") || path.contains("/register")) {
            return chain.filter(exchange);
        }

        // Check Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            // For now, allow all to make 76 work - Later enable strict check for 73 integration
            System.out.println("JWT Filter: No token found for " + path + " - Allowing for development (Enable strict in production)");
            return chain.filter(exchange);

            // Production code - Uncomment after 73 Auth complete:
            // exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            // return exchange.getResponse().setComplete();
        }

        // Here you would validate token with 73-auth-service
        // For now just log and allow
        System.out.println("JWT Filter: Token found - " + authHeader.substring(0, 20) + "...");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}