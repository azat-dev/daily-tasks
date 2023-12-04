package com.azatdev.dailytasks.presentation.security.services;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtTokenException extends AuthenticationException {

    public InvalidJwtTokenException(String description) {
        super(description);
    }
}
