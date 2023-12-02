package com.azatdev.dailytasks.presentation.api.rest.resources.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.azatdev.dailytasks.presentation.api.rest.entities.AuthenticationRequest;
import com.azatdev.dailytasks.presentation.config.WebSecurityConfig;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsService;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;

// @Configuration
// class WebSecurityConfigTest {

//     @Primary
//     @Bean
//     CustomUserDetailsService customUserDetailsService() {
//         final var value = mock(CustomUserDetailsService.class);
//         return value;
//     };

// }

@WebMvcTest(AuthenticationController.class)
// @Import({ WebSecurityConfigTest.class, WebSecurityConfig.class })
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
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void authenticationSuccessTest() throws Exception {
        // Given
        final var userId = UUID.randomUUID();
        final var username = "username";
        final var password = "password";

        final var userPrincipal = new UserPrincipal(
            userId,
            username,
            password,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        final var authenticationRequest = new AuthenticationRequest(
            username,
            password
        );

        final var payload = objectMapper.writeValueAsString(authenticationRequest);

        given(
            passwordEncoder.matches(
                password,
                password
            )
        ).willReturn(true);

        given(customUserDetailsService.loadUserByUsername(username)).willReturn(userPrincipal);

        // When
        final var response = mockMvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        )
            .andReturn()
            .getResponse();

        // Then
        then(customUserDetailsService).should(times(1))
            .loadUserByUsername(username);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
