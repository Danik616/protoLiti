package com.spring.prototipolitigando.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.spring.prototipolitigando.exceptions.UnauthorizedException;
import com.spring.prototipolitigando.service.JwtUtil;
import reactor.core.publisher.Mono;


@Component
public class JwtRequestFilter implements WebFilter {
@Value("${jwt.secret}")
private String jwtSecret;

private final JwtUtil jwtUtil;
    
public JwtRequestFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
}

@Override
public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    
    if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
        String token = authorizationHeader.substring(7);
        
        return jwtUtil.validateToken(token)
            .flatMap(isValid -> {
                if (isValid) {
                    String username = jwtUtil.extractUsername(token);
                    return jwtUtil.generateToken(username)
                        .flatMap(newToken -> {
                            ServerHttpRequest request = exchange.getRequest().mutate()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newToken)
                                .build();
                            ServerWebExchange newExchange = exchange.mutate().request(request).build();
                            return chain.filter(newExchange);
                        });
                } else {
                    return Mono.error(new UnauthorizedException("Invalid token"));
                }
            });
    } else {
        return Mono.error(new UnauthorizedException("Authorization header not found"));
    }
}
}
