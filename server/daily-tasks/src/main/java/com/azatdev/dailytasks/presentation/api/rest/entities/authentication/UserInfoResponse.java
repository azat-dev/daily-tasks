package com.azatdev.dailytasks.presentation.api.rest.entities.authentication;

import com.azatdev.dailytasks.domain.models.AppUser;

import jakarta.validation.constraints.NotBlank;

public record UserInfoResponse(@NotBlank String username) {

    public static UserInfoResponse from(AppUser appUser) {
        return new UserInfoResponse(appUser.username());
    }
}
