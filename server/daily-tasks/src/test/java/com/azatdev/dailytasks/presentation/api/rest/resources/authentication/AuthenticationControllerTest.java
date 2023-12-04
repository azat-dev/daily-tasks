package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.azatdev.dailytasks.domain.models.AppUser;
import com.azatdev.dailytasks.domain.usecases.SignUpAppUserUseCase;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.AuthenticationRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.RefreshTokenRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.SignUpRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.authentication.TokenVerificationRequest;
import com.azatdev.dailytasks.presentation.config.presentation.security.WebSecurityConfig;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;
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
    private AuthenticationManager authenticationManager;

    @MockBean
    private SignUpAppUserUseCase signUpAppUserUseCase;

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
        response.andExpect(status().isUnauthorized())
            .andExpect(unauthenticated());
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

        given(authenticationManager.authenticate(any())).willThrow(new BadCredentialsException("Wrong password"));

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        response.andExpect(status().isUnauthorized())
            .andExpect(unauthenticated());
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
        response.andExpect(status().isBadRequest())
            .andExpect(unauthenticated());
    }

    @Test
    void authenticate_givenCredentialsValid_thenReturnTokensAndAuthenticateUser() throws Exception {

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

        given(tokenProvider.generateAccessToken(user.getId())).willReturn(expectedAccessToken);
        given(tokenProvider.generateRefreshToken(user.getId())).willReturn(expectedRefreshToken);

        given(
            passwordEncoder.matches(
                (CharSequence) password,
                password
            )
        ).willReturn(true);

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        then(authenticationManager).should(times(1))
            .authenticate(
                new UsernamePasswordAuthenticationToken(
                    username,
                    password
                )
            );

        response.andExpect(status().isOk())
            .andExpect(authenticated())
            .andExpect(jsonPath("$.access").value(expectedAccessToken))
            .andExpect(jsonPath("$.refresh").value(expectedRefreshToken));
    }

    @Test
    void verifyToken_givenEmptyToken_thenReturnError() throws Exception {

        // Given
        final var emptyToken = "";

        given(tokenProvider.verifyToken(emptyToken)).willReturn(false);

        final var request = new TokenVerificationRequest(emptyToken);

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

        final var request = new TokenVerificationRequest(wrongToken);

        // When
        final var response = performVerifyTokenRequest(request);

        // Then
        response.andExpect(status().isUnauthorized());

        then(tokenProvider).should(times(1))
            .verifyToken(wrongToken);
    }

    @Test
    void verifyToken_givenValidToken_thenReturnSuccessStatusCode() throws Exception {

        // Given
        final var validToken = "validToken";

        given(tokenProvider.verifyToken(validToken)).willReturn(true);

        final var request = new TokenVerificationRequest(validToken);

        // When
        final var response = performVerifyTokenRequest(request);

        // Then
        response.andExpect(status().isOk());

        then(tokenProvider).should(times(1))
            .verifyToken(validToken);
    }

    @Test
    void refreshToken_givenEmptyToken_thenReturnError() throws Exception {

        // Given
        final var emptyToken = "";

        given(tokenProvider.verifyToken(emptyToken)).willReturn(true);

        final var request = new RefreshTokenRequest(emptyToken);

        // When
        final var response = performRefreshTokenRequest(request);

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    void refreshToken_givenWrongToken_thenReturnError() throws Exception {

        // Given
        final var wrongToken = "wrongToken";

        given(tokenProvider.verifyToken(wrongToken)).willReturn(false);

        final var request = new RefreshTokenRequest(wrongToken);

        // When
        final var response = performRefreshTokenRequest(request);

        // Then
        response.andExpect(status().isUnauthorized());
    }

    UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void refreshToken_givenValidToken_thenReturnNewTokenPair() throws Exception {

        // Given
        final var validToken = "validToken";

        final var newRefreshToken = "newRefreshToken";

        final var newAccessToken = "newAccessToken";

        final var userId = anyUserId();

        given(tokenProvider.verifyToken(validToken)).willReturn(true);

        given(tokenProvider.getUserIdFromToken(validToken)).willReturn(userId);

        given(tokenProvider.generateAccessToken(userId)).willReturn(newAccessToken);

        given(tokenProvider.generateRefreshToken(userId)).willReturn(newRefreshToken);

        final var request = new RefreshTokenRequest(validToken);

        // When
        final var response = performRefreshTokenRequest(request);

        // Then
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.access").value(newAccessToken));
        response.andExpect(jsonPath("$.refresh").value(newRefreshToken));
    }

    @Test
    void singUp_givenNotMatchingPasswords_thenReturnError() throws Exception {

        // Given
        final var username = "username";
        final var password1 = "password";
        final var password2 = "password2";

        final var request = new SignUpRequest(
            username,
            password1,
            password2
        );

        // When
        final var response = performSignUpRequest(request);

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    void singUp_givenValidCredentials_thenCreateANewUser() throws Exception {

        // Given
        final var userId = anyUserId();
        final var username = "username";
        final var password = "password";

        final var request = new SignUpRequest(
            username,
            password,
            password
        );

        given(passwordEncoder.encode(any())).willReturn(password);

        final var user = new AppUser(
            userId,
            username,
            password
        );

        given(
            signUpAppUserUseCase.execute(
                username,
                password
            )
        ).willReturn(user);

        final var expectedAccessToken = "accessToken";
        final var expectedRefreshToken = "refreshToken";

        given(tokenProvider.generateAccessToken(userId)).willReturn(expectedAccessToken);
        given(tokenProvider.generateRefreshToken(userId)).willReturn(expectedRefreshToken);

        final var userPrincipal = new UserPrincipal(
            userId,
            username,
            password,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        final var usernamePasswordToken = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            password,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        given(authenticationManager.authenticate(any())).willReturn(usernamePasswordToken);

        // When
        final var action = performSignUpRequest(request);

        // Then
        then(passwordEncoder).should(times(1))
            .encode(matches(password));

        action.andExpect(status().isCreated())
            .andExpect(authenticated());

        action.andExpect(jsonPath("$.userInfo.username").value(username));
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

        given(authenticationManager.authenticate(any())).willReturn(
            new UsernamePasswordAuthenticationToken(
                userPrincipal,
                password,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            )
        );

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
        final String url = "/api/public/auth/token";
        return performPostRequest(
            url,
            request
        );
    }

    private ResultActions performVerifyTokenRequest(TokenVerificationRequest request) throws Exception {
        final String url = "/api/public/auth/token/verify";
        return performPostRequest(
            url,
            request
        );
    }

    private ResultActions performRefreshTokenRequest(RefreshTokenRequest request) throws Exception {
        final String url = "/api/public/auth/token/refresh";
        return performPostRequest(
            url,
            request
        );
    }

    private ResultActions performSignUpRequest(SignUpRequest request) throws Exception {
        final String url = "/api/public/auth/sign-up";
        return performPostRequest(
            url,
            request
        );
    }
}
