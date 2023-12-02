package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azatdev.dailytasks.presentation.api.rest.entities.AuthenticationRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.AuthenticationResponse;

@RestController
@RequestMapping("/api/auth")
public interface AuthenticationResource {

    @PostMapping("/token")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest);
}
