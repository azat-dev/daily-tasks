package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.AuthenticationRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.AuthenticationResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.RefreshTokenRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.RefreshTokenResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.SignUpRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.TokenVerificationRequest;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Component
public class AuthenticationController implements AuthenticationResource {

    @Autowired
    private JWTService tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(
        AuthenticationRequest authenticationRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        final var username = authenticationRequest.username();

        final var authenticationToken = new UsernamePasswordAuthenticationToken(
            username,
            authenticationRequest.password()
        );

        try {
            final var authentication = authenticationManager.authenticate(authenticationToken);
            final var userPrincipal = (UserPrincipal) authentication.getPrincipal();

            if (userPrincipal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                    .build();
            }

            final var userId = userPrincipal.getId();

            final var authenticationResponse = new AuthenticationResponse(
                tokenProvider.generateAccessToken(userId),
                tokenProvider.generateRefreshToken(userId)
            );

            final var context = SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);

            this.securityContextRepository.saveContext(
                context,
                request,
                response
            );

            return ResponseEntity.ok(authenticationResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .build();
        }
    }

    @Override
    public ResponseEntity<Void> verifyToken(@Valid TokenVerificationRequest tokenVerificationRequest) {

        final var isValid = tokenProvider.verifyToken(tokenVerificationRequest.token());

        if (isValid) {
            return ResponseEntity.ok()
                .build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
            .build();
    }

    @Override
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {

        final var token = refreshTokenRequest.refreshToken();
        final var isValid = tokenProvider.verifyToken(token);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .build();
        }

        final var userId = tokenProvider.getUserIdFromToken(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .build();
        }

        final var newAccessToken = tokenProvider.generateAccessToken(userId);
        final var newRefreshToken = tokenProvider.generateRefreshToken(userId);

        final var response = new RefreshTokenResponse(
            newAccessToken,
            newRefreshToken
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> signUp(@Valid SignUpRequest signUpRequest) {

        final var doesPasswordsMatch =  signUpRequest.password1() == signUpRequest.password2();

        if (!doesPasswordsMatch) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .build();
        }
        
        // TODO Auto-generated method stub
        return null;
    }
}
