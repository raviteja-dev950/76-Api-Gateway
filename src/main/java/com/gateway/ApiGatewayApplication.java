package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("=================================================");
        System.out.println("76 - API GATEWAY RUNNING ON 8084 - TIER 8");
        System.out.println("Routes: /api/auth/** -> 8081, /api/products/** -> 8082, /api/orders/** -> 8086");
        System.out.println("Test: http://localhost:8084/actuator/gateway/routes");
        System.out.println("=================================================");
    }
}