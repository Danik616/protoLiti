package com.spring.prototipolitigando.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;


@Component
public class JwtRequestFilter implements WebFilter {
@Value("${jwt.secret}")
private String jwtSecret;

private final ReactiveUserDetailsService userDetailsService;
private final ServerSecurityContextRepository securityContextRepository;

public JwtRequestFilter(ReactiveUserDetailsService userDetailsService,
                        ServerSecurityContextRepository securityContextRepository) {
    this.userDetailsService = userDetailsService;
    this.securityContextRepository = securityContextRepository;
}

@Override
public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    final String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

    String username = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
            Claims claims = jwtParser.parseClaimsJws(jwt).getBody();
            username = claims.getSubject();
        } catch (ExpiredJwtException e) {
            return exchange.getResponse().setComplete()
    .onErrorResume(ExpiredJwtException.class, ex -> {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return Mono.empty();
    });
        }
    }

    if (username != null) {
        return this.userDetailsService.findByUsername(username)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))
                .flatMap(authentication -> {
                    SecurityContext context = new SecurityContextImpl(authentication);
                    return this.securityContextRepository.save(exchange, context);
                })
                .flatMap(v -> chain.filter(exchange));
    } else {
        return chain.filter(exchange);
    }
}
}
