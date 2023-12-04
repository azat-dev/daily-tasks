package com.azatdev.dailytasks.presentation.api.rest.entities.authentication;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
    @NotBlank @Length(min = 3, max = 255) String username,
    @NotBlank @Length(min = 8, max = 255) String password1,
    @NotBlank @Length(min = 8, max = 255) String password2
) {
}
