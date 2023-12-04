package com.azatdev.dailytasks.presentation.api.rest.entities.authentication;

import jakarta.validation.constraints.NotNull;

public record SignUpResponse(
    @NotNull AuthenticationResponse authenticationInfo,
    @NotNull UserInfoResponse userInfo
) {

}
