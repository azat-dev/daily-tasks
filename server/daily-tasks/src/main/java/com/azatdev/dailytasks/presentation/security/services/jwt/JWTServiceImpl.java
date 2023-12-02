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
    private int jwtExpirationInMs;

    public JWTServiceImpl(
        String jwtSecret,
        int jwtExpirationInMs
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
    public boolean verifyToken(String authToken) {
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
