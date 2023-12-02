package com.azatdev.dailytasks.presentation.api.rest.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
    @Min(3) @NotBlank String username,

    @Min(6) @Max(255) @NotBlank String password
) {
}
