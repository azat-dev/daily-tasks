package com.azatdev.dailytasks.presentation.api.rest.entities.authentication;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) {

}
