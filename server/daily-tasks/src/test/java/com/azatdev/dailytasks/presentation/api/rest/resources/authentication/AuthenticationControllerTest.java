package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.AuthenticationRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.TokenVerificationRequest;
import com.azatdev.dailytasks.presentation.config.WebSecurityConfig;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsService;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;

@WebMvcTest(AuthenticationController.class)
@Import(WebSecurityConfig.class)
class AuthenticationControllerTest {

    @MockBean
    private JWTService tokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Test
    void authenticate_givenUserNotExists_thenReturnError() throws Exception {

        // Given
        final var authenticationRequest = new AuthenticationRequest(
            "username",
            "password"
        );

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        response.andExpect(status().isUnauthorized());
    }

    @Test
    void authenticate_givenExistingUserWrongPassword_thenReturnError() throws Exception {

        // Given
        final var user = givenExistingPrincipal();
        final var wrongPassword = user.getUsername() + "wrongPassword";

        final var authenticationRequest = new AuthenticationRequest(
            user.getUsername(),
            wrongPassword
        );

        given(
            passwordEncoder.matches(
                anyString(),
                anyString()
            )
        ).willReturn(false);

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        response.andExpect(status().isUnauthorized());
    }

    @Test
    void authenticate_givenEmptyPassword_thenReturnError() throws Exception {

        // Given
        final var user = givenExistingPrincipal();
        final var notValidPassword = "";

        final var authenticationRequest = new AuthenticationRequest(
            user.getUsername(),
            notValidPassword
        );

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    void authenticate_givenCredentialsValid_thenReturnTokens() throws Exception {

        // Given
        final var user = givenExistingPrincipal();

        final var authenticationRequest = new AuthenticationRequest(
            user.getUsername(),
            user.getPassword()
        );

        final var username = user.getUsername();
        final var password = user.getPassword();

        final var expectedAccessToken = "accessToken";
        final var expectedRefreshToken = "refreshToken";

        given(tokenProvider.generateToken(user.getId())).willReturn(expectedAccessToken);

        given(
            passwordEncoder.matches(
                (CharSequence) password,
                password
            )
        ).willReturn(true);

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        then(passwordEncoder).should(times(1))
            .matches(
                password,
                password
            );

        then(customUserDetailsService).should(times(1))
            .loadUserByUsername(username);

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.access").value(expectedAccessToken))
            .andExpect(jsonPath("$.refresh").value(expectedRefreshToken));
    }

    @Test
    void verifyToken_givenEmptyToken_thenReturnError() throws Exception {

        // Given
        final var emptyToken = "";

        given(tokenProvider.verifyToken(emptyToken)).willReturn(false);

        TokenVerificationRequest request = new TokenVerificationRequest(
            emptyToken
        );

        // When
        final var response = performVerifyTokenRequest(request);

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    void verifyToken_givenWrongToken_thenReturnError() throws Exception {

        // Given
        final var wrongToken = "wrongToken";

        given(tokenProvider.verifyToken(wrongToken)).willReturn(false);

        TokenVerificationRequest request = new TokenVerificationRequest(
            wrongToken
        );

        // When
        final var response = performVerifyTokenRequest(request);

        // Then
        response.andExpect(status().isUnauthorized());

        then(tokenProvider).should(times(1))
            .verifyToken(wrongToken);
    }

    @Test
    void verifyToken_givenValidToken_thenReturnError() throws Exception {

        // Given
        final var validToken = "validToken";

        given(tokenProvider.verifyToken(validToken)).willReturn(true);

        TokenVerificationRequest request = new TokenVerificationRequest(
            validToken
        );

        // When
        final var response = performVerifyTokenRequest(request);

        // Then
        response.andExpect(status().isUnauthorized());

        then(tokenProvider).should(times(1))
            .verifyToken(validToken);
    }

    // Helpers

    private UserPrincipal givenExistingPrincipal() {

        final var userId = UUID.randomUUID();
        final var username = "username";
        final var password = "password";

        final var userPrincipal = new UserPrincipal(
            userId,
            username,
            password,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        given(customUserDetailsService.loadUserByUsername(username)).willReturn(userPrincipal);
        return userPrincipal;
    }

    private ResultActions performPostRequest(
        String url,
        Object request
    ) throws Exception {

        final var payload = objectMapper.writeValueAsString(request);

        return mockMvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        );
    }

    private ResultActions performAuthenticateRequest(AuthenticationRequest request) throws Exception {
        final String url = "/api/auth/token";
        return performPostRequest(
            url,
            request
        );
    }

    private ResultActions performVerifyTokenRequest(TokenVerificationRequest request) throws Exception {
        final String url = "/api/auth/token/verify";
        return performPostRequest(
            url,
            request
        );
    }
}
