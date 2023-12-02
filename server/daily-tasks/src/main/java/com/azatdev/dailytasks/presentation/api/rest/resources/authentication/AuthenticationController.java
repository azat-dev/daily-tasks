package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.azatdev.dailytasks.presentation.api.rest.entities.AuthenticationRequest;

@Component
public class AuthenticationController implements AuthenticationResource {

    @Override
    public ResponseEntity<AuthenticationRequest> authenticate(AuthenticationRequest authenticationRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticate'");
    }

}
