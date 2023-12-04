package com.azatdev.dailytasks.presentation.security.services;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azatdev.dailytasks.presentation.config.WebSecurityConfig;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;

@RestController
class TestController {

    @PostMapping("/api/test_path")
    ResponseEntity<String> testPath(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(
            userPrincipal.getId()
                .toString()
        );
    }
}

@WebMvcTest(TestController.class)
@Import(WebSecurityConfig.class)
class JwtAuthenticationTest {

    @MockBean
    private JWTService tokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenNoToken_thenReturnError() throws Exception {

        // Given
        final String token = null;

        // When
        final var action = performRequestWithToken(token);

        // Then
        action.andExpect(status().isForbidden())
            .andExpect(unauthenticated());
    }

    @Test
    void givenNoValidToken_thenReturnError() throws Exception {

        // Given
        final var token = "someToken";
        given(tokenProvider.verifyToken(token)).willReturn(false);

        // When
        final var action = performRequestWithToken(token);

        // Then
        then(tokenProvider).should(times(1))
            .verifyToken(token);

        action.andExpect(status().isForbidden())
            .andExpect(unauthenticated());
    }

    @Test
    void givenValidToken_thenAuthenticate() throws Exception {

        // Given
        final var token = "someToken";

        final var userPrincipal = givenExistingPrincipal();
        final var userId = userPrincipal.getId();

        given(tokenProvider.verifyToken(token)).willReturn(true);

        given(tokenProvider.getUserIdFromToken(token)).willReturn(userId);

        given(customUserDetailsService.loadUserById(userId)).willReturn(userPrincipal);

        // When
        final var action = performRequestWithToken(token);

        // Then
        then(tokenProvider).should(times(1))
            .verifyToken(token);

        then(tokenProvider).should(times(1))
            .getUserIdFromToken(token);

        then(customUserDetailsService).should(times(1))
            .loadUserById(userId);

        action.andExpect(status().isOk())
            .andExpect(authenticated())
            .andExpect(content().string(userId.toString()));
    }

    UUID anyUserId() {
        return UUID.randomUUID();
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

    private ResultActions performRequestWithToken(String token) throws Exception {

        final var url = "/api/test_path";

        var request = post(url);

        if (token != null) {

            request = request.header(
                "Authorization",
                "Bearer " + token
            );
        }
        return mockMvc.perform(request);
    }
}
