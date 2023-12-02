package com.azatdev.dailytasks.presentation.security.services.jwt;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public interface JWTService {

    public String generateToken(UUID userId);

    public boolean verifyToken(String authToken);

    public UUID getUserIdFromToken(String token);
}
