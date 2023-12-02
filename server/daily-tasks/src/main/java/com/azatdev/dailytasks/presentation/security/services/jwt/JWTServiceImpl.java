package com.azatdev.dailytasks.presentation.security.services.jwt;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

public class JWTServiceImpl implements JWTService {

    private SecretKey secretKey;
    private long accessTokenExpirationInMs;
    private long refreshTokenExpirationInMs;
    private DateTimeProvider dateTimeProvider;

    public JWTServiceImpl(
        String jwtSecret,
        long accessTokenExpirationInMs,
        long refreshTokenExpirationInMs,
        DateTimeProvider dateTimeProvider
    ) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.accessTokenExpirationInMs = accessTokenExpirationInMs;
        this.refreshTokenExpirationInMs = refreshTokenExpirationInMs;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public String generateAccessToken(UUID userId) {
        long now = dateTimeProvider.now();
        Date expiryDate = new Date(now + accessTokenExpirationInMs);

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(new Date(now))
            .expiration(expiryDate)
            .signWith(
                secretKey,
                Jwts.SIG.HS512
            )
            .compact();
    }

    @Override
    public String generateRefreshToken(UUID userId) {
        long now = dateTimeProvider.now();
        Date expiryDate = new Date(now + refreshTokenExpirationInMs);

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(new Date(now))
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
            .clock(() -> new Date(dateTimeProvider.now()))
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();

        return UUID.fromString(encodedUserId);
    }

    @Override
    public boolean verifyToken(String authToken) {
        try {
            Jwts.parser()
                .clock(() -> new Date(dateTimeProvider.now()))
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
