package com.industry_connect.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouterConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("identity-service", r -> r.path("/api/auth/**", "/api/organizations/**")
                        .uri("lb://identity-service"))
                .route("marketplace-service", r -> r.path("/api/listings/**", "/api/offers/**")
                        .uri("lb://marketplace-service"))
                .route("order-service", r -> r.path("/api/orders/**", "/api/shipments/**")
                        .uri("lb://order-service"))
                .route("search-service", r -> r.path("/api/search/**")
                        .uri("lb://search-service"))
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .uri("lb://notification-service"))
                .route("analytics-service", r -> r.path("/api/analytics/**")
                        .uri("lb://analytics-service"))
                .build();
    }
}
