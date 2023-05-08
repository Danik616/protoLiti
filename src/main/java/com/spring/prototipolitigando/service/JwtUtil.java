package com.spring.prototipolitigando.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private final ReactiveUserDetailsService userDetailsService;

    private SecretKey generateKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public JwtUtil(ReactiveUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Mono<String> generateToken(String username) {
        return userDetailsService.findByUsername(username)
                .flatMap(userDetails -> Mono.just(createToken(new HashMap<>(), userDetails.getUsername())));
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Mono<Boolean> validateToken(String token) {
        String username = extractUsername(token);
        return userDetailsService.findByUsername(username)
                .flatMap(userDetails -> Mono.just(username.equals(userDetails.getUsername()) && !isTokenExpired(token)));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(generateKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
