package com.azatdev.dailytasks.presentation.api.rest.entities.authentication;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenResponse(
    @NotBlank String access,
    @NotBlank String refresh
) {

}
