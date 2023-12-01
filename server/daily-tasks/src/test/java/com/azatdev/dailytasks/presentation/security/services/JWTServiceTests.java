package com.azatdev.dailytasks.presentation.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

class JWTServiceImpl implements JWTService {

    private SecretKey secretKey;
    private int jwtExpirationInMs;

    public JWTServiceImpl(
        @Value("${app.security.jwtSecret}") String jwtSecret,
        @Value("${app.security.jwtExpirationInMs}") int jwtExpirationInMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    @Override
    public String generateToken(UUID userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(
                secretKey,
                Jwts.SIG.HS512
            )
            .compact();
    }

    @Override
    public UUID getUserIdFromToken(String token) {

        final var encodedUserId = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();

        return UUID.fromString(encodedUserId);
    }

    @Override
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException ex) {
            // log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            // log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // log.error("JWT claims string is empty.");
        }
        return false;
    }

}

class JWTServiceTests {

    private final JWTService sut = new JWTServiceImpl(
        "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret",
        10000
    );

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
        assertThrows(
            Exception.class,
            () -> sut.getUserIdFromToken(wrongToken)
        );

        // Then
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
