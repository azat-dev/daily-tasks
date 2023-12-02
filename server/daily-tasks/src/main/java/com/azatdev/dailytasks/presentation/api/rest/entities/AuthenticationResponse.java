package com.azatdev.dailytasks.presentation.api.rest.entities;

public record AuthenticationResponse(
    String access,
    String refresh
) {
}
