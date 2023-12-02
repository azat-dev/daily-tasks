package com.azatdev.dailytasks.presentation.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTServiceImpl;

class JWTServiceTests {

    private final JWTService sut = new JWTServiceImpl(
        "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret",
        10000
    );

    @Test
    void generateTokenMustReturnTokenTest() {

        // Given
        final var userId = UUID.randomUUID();

        // When
        final var result = sut.generateToken(userId);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void validateTokenMustFailOnWrongTokenTest() {

        // Given
        final var wrongToken = "wrongToken";

        // When
        final var isValid = sut.verifyToken(wrongToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void validateTokenMustSuccedOnCorrectTokenTest() {

        // Given
        final var token = sut.generateToken(UUID.randomUUID());

        // When
        final var isValid = sut.verifyToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void getUserIdFromTokenMustFailOnWrongToken() {

        // Given
        final var wrongToken = "wrongToken";

        // When
        assertThrows(
            Exception.class,
            () -> sut.getUserIdFromToken(wrongToken)
        );

        // Then
    }

    @Test
    void getUserIdFromTokenMustReturnUserIdOnCorrectToken() {

        // Given
        final var userId = UUID.randomUUID();
        final var token = sut.generateToken(userId);

        // When
        final var result = sut.getUserIdFromToken(token);

        // Then
        assertThat(result).isEqualTo(userId);
    }
}
