package com.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LoggingFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String method = exchange.getRequest().getMethod().toString();
        String path = exchange.getRequest().getURI().getPath();
        String ip = exchange.getRequest().getRemoteAddress()!= null? exchange.getRequest().getRemoteAddress().toString() : "unknown";

        System.out.println("[" + time + "] GATEWAY LOG -> " + method + " " + path + " from " + ip);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            int status = exchange.getResponse().getStatusCode()!= null? exchange.getResponse().getStatusCode().value() : 0;
            System.out.println("[" + time + "] GATEWAY RESPONSE -> " + method + " " + path + " => Status: " + status);
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Run first
    }
}