package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.AuthenticationRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.AuthenticationResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public interface AuthenticationResource {

    @PostMapping("/token")
    ResponseEntity<AuthenticationResponse> authenticate(
        @Valid @RequestBody AuthenticationRequest authenticationRequest
    );
}
