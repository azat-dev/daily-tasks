package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.azatdev.dailytasks.presentation.api.rest.entities.AuthenticationRequest;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsService;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;

@Component
public class AuthenticationController implements AuthenticationResource {

    @Autowired
    private JWTService tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public ResponseEntity<AuthenticationRequest> authenticate(AuthenticationRequest authenticationRequest) {
        
        final var username = authenticationRequest.username();

        final var authentication = new UsernamePasswordAuthenticationToken(
            username,
            authenticationRequest.password()
        );

        final var user = customUserDetailsService.loadUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
        }

        authentication.setDetails(user);

        SecurityContextHolder.getContext()
            .setAuthentication(authentication);

        return null;
    }
}
