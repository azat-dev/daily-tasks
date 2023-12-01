package com.azatdev.dailytasks.presentation.security.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

class JWTServiceImpl implements JWTService {


    private String jwtSecret;
    private int jwtExpirationInMs;

    public JWTServiceImpl(
        @Value("${app.security.jwtSecret}") String jwtSecret,
        @Value("${app.security.jwtExpirationInMs}") int jwtExpirationInMs
    ) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    @Override
    public String generateToken(UUID userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        final var key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public UUID getUserIdFromToken(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean validateToken(String authToken) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateToken'");
    }

}

class JWTServiceTests {

    private final JWTService sut = new JWTServiceImpl("secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret", 10000);

    @Test
    void generateTokenMustReturnTokenTest() {
    
        // Given
        final var userId = UUID.randomUUID();
    
        // When
        final var result = sut.generateToken(userId);
    
        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void validateTokenMustFailOnWrongTokenTest() {
    
        // Given
        final var wrongToken = "wrongToken";

        // When
        final var isValid = sut.validateToken(wrongToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void validateTokenMustSuccedOnCorrectTokenTest() {
    
        // Given
        final var token = sut.generateToken(UUID.randomUUID());

        // When
        final var isValid = sut.validateToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void getUserIdFromTokenMustFailOnWrongToken() {
    
        // Given
        final var wrongToken = "wrongToken";

        // When
        final var userId = sut.getUserIdFromToken(wrongToken);

        // Then
        assertThat(userId).isNull();
    }

    @Test
    void getUserIdFromTokenMustReturnUserIdOnCorrectToken() {
    
        // Given
        final var userId = UUID.randomUUID();
        final var token = sut.generateToken(userId);

        // When
        final var result = sut.getUserIdFromToken(token);

        // Then
        assertThat(result).isEqualTo(userId);
    }
}
