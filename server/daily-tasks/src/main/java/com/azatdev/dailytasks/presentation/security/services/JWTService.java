package com.azatdev.dailytasks.presentation.security.services;

import java.util.UUID;

public interface JWTService {

    public String generateToken(UUID userId);

    public boolean validateToken(String authToken);

    public UUID getUserIdFromToken(String token);
}