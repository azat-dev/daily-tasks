package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import com.azatdev.dailytasks.domain.usecases.SignUpAppUserUseCase;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.AuthenticationRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.AuthenticationResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.RefreshTokenRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.RefreshTokenResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.SignUpRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.SignUpResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.TokenVerificationRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.UserInfoResponse;
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

    @Autowired
    private SignUpAppUserUseCase signUpAppUserUseCase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthenticationResponse authenticateUser(
        String username,
        String password,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {

        final var authenticationRequest = new UsernamePasswordAuthenticationToken(
            username,
            password
        );

        final var authentication = authenticationManager.authenticate(authenticationRequest);
        final var userPrincipal = (UserPrincipal) authentication.getPrincipal();

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

        return authenticationResponse;
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(
        AuthenticationRequest authenticationRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        try {
            final var authenticationResponse = this.authenticateUser(
                authenticationRequest.username(),
                authenticationRequest.password(),
                request,
                response
            );
            if (authenticationResponse == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                    .build();
            }

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
    public ResponseEntity<SignUpResponse> signUp(
        @Valid SignUpRequest signUpRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        final var username = signUpRequest.username();
        final var doesPasswordsMatch = signUpRequest.password1()
            .equals(signUpRequest.password2());

        if (!doesPasswordsMatch) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .build();
        }

        final var password = signUpRequest.password1();

        final var encodedPassword = passwordEncoder.encode(password);

        try {
            final var appUser = signUpAppUserUseCase.execute(
                username,
                encodedPassword
            );

            final var authenticationResponse = this.authenticateUser(
                username,
                password,
                request,
                response
            );

            final var signUpResponse = new SignUpResponse(
                authenticationResponse,
                UserInfoResponse.from(appUser)
            );

            return ResponseEntity.created(null)
                .body(signUpResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .build();
        }
    }
}
