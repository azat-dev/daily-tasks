package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.azatdev.dailytasks.data.entities.UserData;
import com.azatdev.dailytasks.data.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.data.repositories.TestEntityDataGenerator;

@DataJpaTest
public class JpaUsersRepositoryTest {

    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    TestEntityDataGenerator testData;

    @BeforeEach
    void setUp() {
        testData = new TestEntityDataGenerator();
    }

    @Test
    void saveAndFlush_givenUserWithUserNameExists_thenMustThrowException() {

        // Given
        final var existingUserName = "existingUserName";

        final var existingUser = new UserData(
            UUID.randomUUID(),
            existingUserName,
            "password"
        );

        jpaUsersRepository.saveAndFlush(existingUser);

        final var newUser = new UserData(
            UUID.randomUUID(),
            existingUserName,
            "password"
        );

        // When
        final var exception = assertThrows(
            DataIntegrityViolationException.class,
            () -> jpaUsersRepository.saveAndFlush(newUser)
        );

        // Then
        assertThat(exception).isNotNull();
    }
}
