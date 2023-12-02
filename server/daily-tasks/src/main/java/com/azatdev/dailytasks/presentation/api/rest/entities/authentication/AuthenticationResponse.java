package com.azatdev.dailytasks.presentation.api.rest.entities.authentication;

public record AuthenticationResponse(
    String access,
    String refresh
) {
}
