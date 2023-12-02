package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.azatdev.dailytasks.presentation.api.rest.entities.AuthenticationRequest;
import com.azatdev.dailytasks.presentation.config.WebSecurityConfig;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsService;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;

@WebMvcTest(AuthenticationController.class)
@Import(WebSecurityConfig.class)
class AuthenticationControllerTest {

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JWTService tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final String url = "/api/auth/token";

    @Test
    void authenticationFailedTest() throws Exception {
        // Given
        final var authenticationRequest = new AuthenticationRequest(
            "username",
            "password"
        );

        final var payload = objectMapper.writeValueAsString(authenticationRequest);

        // When
        final var response = mockMvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        )
            .andReturn()
            .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
