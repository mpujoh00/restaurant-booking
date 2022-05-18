package com.restaurant.booking.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    private final AuthenticationFilter authenticationFilter;
    private static final String USER_SERVICE = "lb://user-service";
    private static final String RESTAURANT_SERVICE = "lb://restaurant-service";
    private static final String TABLE_SERVICE = "lb://table-service";

    @Autowired
    public ApiGatewayConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){
        return builder.routes()
                .route(p -> p.path("/api/v1/auth/**")
                        //.filters(f -> f.filter(authenticationFilter))
                        .uri(USER_SERVICE))
                .route(p -> p.path("/api/v1/users/**")
                        //.filters(f -> f.filter(authenticationFilter))
                        .uri(USER_SERVICE))
                .route(p -> p.path("/api/v1/admin/users/**")
                        //.filters(f -> f.filter(authenticationFilter))
                        .uri(USER_SERVICE))
                .route(p -> p
                        .path("/api/v1/restaurants/**")
                        //.filters(f -> f.filter(authenticationFilter))
                        .uri(RESTAURANT_SERVICE))
                .route(p -> p.path("/api/v1/admin/restaurants/**")
                        //.filters(f -> f.filter(authenticationFilter))
                        .uri(RESTAURANT_SERVICE))
                .route(p -> p.path("/api/v1/tables/**")
                        //.filters(f -> f.filter(authenticationFilter))
                        .uri(TABLE_SERVICE))
                .build();
    }

}
