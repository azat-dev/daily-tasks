package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.azatdev.dailytasks.data.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.data.repositories.TestEntityDataGenerator;

@DataJpaTest
@Import(TestEntityDataGenerator.class)
class JpaActivitySessionsRepositoryTest {

    @Autowired
    JpaActivitySessionsRepository jpaActivitySessionRepository;

    @Autowired
    TestEntityDataGenerator testData;

    @Test
    void findByOwnerIdAndTaskIdAndFinishedAt_givenExistingSession_thenMustReturnCorrectSession() {

        // Given
        final var correctOwner = testData.givenExistingUser("correctOwner");
        final var backlog = testData.givenExistingDayBacklog(correctOwner);
        final var correctFinishedAt = ZonedDateTime.now();

        final var correctTask = testData.givenExistingTaskData(
            correctOwner,
            backlog,
            0
        );

        final var wrongOwner = testData.givenExistingUser("wrongOwner");
        final var wrongBacklog = testData.givenExistingDayBacklog(wrongOwner);
        final var wrongFinishedAt = correctFinishedAt.plusDays(1);

        final var wrongTask = testData.givenExistingTaskData(
            wrongOwner,
            wrongBacklog,
            0
        );

        final var correctActivitySession = testData.givenExistingActivitySession(
            correctOwner,
            correctTask,
            ZonedDateTime.now(),
            correctFinishedAt
        );

        final var wrongOwnerActivitySession = testData.givenExistingActivitySession(
            wrongOwner,
            correctTask,
            ZonedDateTime.now(),
            correctFinishedAt
        );

        final var wrongTaskActivitySession = testData.givenExistingActivitySession(
            correctOwner,
            wrongTask,
            ZonedDateTime.now(),
            correctFinishedAt
        );

        final var wrongFinishedAtSession = testData.givenExistingActivitySession(
            correctOwner,
            correctTask,
            ZonedDateTime.now(),
            wrongFinishedAt
        );

        // When
        final var foundActivitySession = jpaActivitySessionRepository.findByOwnerIdAndTaskIdAndFinishedAt(
            correctOwner.getId(),
            correctTask.getId(),
            Optional.of(correctFinishedAt)
        );

        // Then
        assertThat(foundActivitySession).isNotEmpty();
        assertThat(foundActivitySession.get()).isEqualTo(correctActivitySession);

        assertThat(foundActivitySession.get()).isNotIn(
            wrongOwnerActivitySession,
            wrongTaskActivitySession,
            wrongFinishedAtSession
        );
    }

    @Test
    void deleteAllByTaskId_givenExistingSessions_thenDeleteMatchedSessions() {

        // Given

        final var user = testData.givenExistingUser("user");

        final var backlog = testData.givenExistingDayBacklog(user);

        final var correctTask = testData.givenExistingTaskData(
            user,
            backlog,
            0
        );

        final var wrongTask = testData.givenExistingTaskData(
            user,
            backlog,
            1
        );

        final var correctActivitySession = testData.givenExistingActivitySession(
            user,
            correctTask,
            ZonedDateTime.now(),
            ZonedDateTime.now()
        );

        final var wrongActivitySession = testData.givenExistingActivitySession(
            user,
            wrongTask,
            ZonedDateTime.now(),
            ZonedDateTime.now()
        );

        // When
        jpaActivitySessionRepository.deleteAllByTaskId(correctTask.getId());

        // Then
        assertThat(jpaActivitySessionRepository.findAll()).containsOnly(wrongActivitySession);
        assertThat(jpaActivitySessionRepository.findAll()).doesNotContain(correctActivitySession);
    }
}
