package com.restaurant.booking.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    private AuthenticationFilter authenticationFilter;

    @Autowired
    public ApiGatewayConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){
        return builder.routes()
                .route(p -> p.path("/api/v1/auth/**")//.filters(f -> f.filter(authenticationFilter))
                        .uri("lb://user-service"))
                .route(p -> p.path("/api/v1/users/**").uri("lb://user-service"))
                .route(p -> p.path("/api/v1/restaurants/**").uri("lb://restaurant-service"))
                .build();
    }

}
