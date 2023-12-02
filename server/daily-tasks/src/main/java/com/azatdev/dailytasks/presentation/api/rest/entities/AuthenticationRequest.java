package com.azatdev.dailytasks.presentation.api.rest.entities;

public record AuthenticationRequest(
    String username,
    String password
) {
}
