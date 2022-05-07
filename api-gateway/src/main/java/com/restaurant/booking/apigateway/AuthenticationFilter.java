package com.restaurant.booking.apigateway;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;

    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticationFilter(RouterValidator routerValidator, JwtUtils jwtUtils) {
        this.routerValidator = routerValidator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // checks if the route is secured
        if (routerValidator.isSecured.test(request)) {
            // looks for authorization header
            if (this.isAuthMissing(request))
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            // gets token
            final String token = this.getAuthHeader(request);

            // checks if the token is correct
            if (jwtUtils.isInvalid(token))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            // valid token
            this.populateRequestWithHeaders(exchange, token);
        }
        return chain.filter(exchange);
    }

    /*PRIVATE*/

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {

        // sets username and role as request's headers
        Claims claims = jwtUtils.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("username", String.valueOf(claims.get("username")))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {

        log.error(err);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}