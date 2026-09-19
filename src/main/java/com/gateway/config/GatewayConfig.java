package com.gateway.config;

import com.gateway.filter.JwtAuthenticationFilter;
import com.gateway.filter.LoggingFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final LoggingFilter loggingFilter;
    private final JwtAuthenticationFilter jwtFilter;

    public GatewayConfig(LoggingFilter loggingFilter, JwtAuthenticationFilter jwtFilter) {
        this.loggingFilter = loggingFilter;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // 75 Order Service - Your service is /api/orders so NO StripPrefix
                .route("order-service", r -> r.path("/api/orders/**")
                        .filters(f -> f.filter(loggingFilter).filter(jwtFilter))
                        .uri("http://localhost:8086"))
                
                // 74 Product Service
                .route("product-service", r -> r.path("/api/products/**")
                        .filters(f -> f.filter(loggingFilter).filter(jwtFilter))
                        .uri("http://localhost:8082"))
                
                // 73 Auth Service - Public
                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(loggingFilter))
                        .uri("http://localhost:8081"))
                .build();
    }
}