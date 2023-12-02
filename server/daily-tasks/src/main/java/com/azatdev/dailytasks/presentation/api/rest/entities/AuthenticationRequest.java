package com.azatdev.dailytasks.presentation.api.rest.entities;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(

    @NotBlank @Length(min = 3, max = 255) String username,
    @NotBlank @Length(min = 3, max = 255) String password
) {
}
