package com.game.apigateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register"
    };

    private JwtUtils jwtUtils;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if(isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authorization = request.getHeaders().getFirst("Authorization");
        if (!authorization.startsWith("Bearer ")) {
            return unAuthorized(exchange);
        }

        String token = authorization.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return unAuthorized(exchange);
        }

        String username = jwtUtils.extractUsername(token);
        Long userId = jwtUtils.extractUserId(token);
        if (username == null || userId == null) {
            return unAuthorized(exchange);
        }

        return chain.filter(exchange
                        .mutate()
                        .request(
                                request.mutate()
                                        .header("X-User-Id", userId.toString())
                                        .header("X-Username", username)
                                        .build())
                        .build());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    public boolean isPublicPath(String path) {
        return Arrays.stream(PUBLIC_ENDPOINTS).anyMatch(t -> t.startsWith(path));
    }

    public Mono<Void> unAuthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
