package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.azatdev.dailytasks.data.repositories.TestEntityDataGenerator;
import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;

@DataJpaTest
class JPABacklogRepositoryTests {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private JPABacklogRepository jpaBacklogRepository;

    private LocalDate anyDate() {
        return LocalDate.of(
            2021,
            1,
            1
        );
    }

    private BacklogData.Duration anyDuration() {
        return BacklogData.Duration.DAY;
    }

    private UUID anyUserId() {
        return UUID.randomUUID();
    }

    private UserData userReference(UUID userId) {
        return entityManager.getEntityManager().getReference(UserData.class, userId);
    }

    private UserData anyExistingUser(String name) {
        return entityManager.persistAndFlush(
            TestEntityDataGenerator.anyUserDataWithUserName(name)
        );
    }

    @Test
    void findByOwnerIdAndStartDateAndDuration_givenEmptyDb_thenShouldReturnNull() {

        // Given
        final var userId = anyUserId();
        final var startDate = anyDate();
        final var duration = anyDuration();

        // Empty DB

        // When
        final var result = jpaBacklogRepository.findByOwnerIdAndStartDateAndDuration(
            userId,
            startDate,
            duration
        );

        // Then
        assertThat(result).isNull();
    }

    @Test
    void findByOwnerIdAndStartDateAndDuration_givenEmptyDb_thenShouldReturnCorrectRecord() {

        // Given
        final var expectedUser = anyExistingUser("expectedUser");
        final var wrongUser = anyExistingUser("wrongUser");

        final var startDate = anyDate();
        final var duration = anyDuration();

        final var expectedBacklog = entityManager.persistFlushFind(
            new BacklogData(
                expectedUser,
                startDate,
                duration
            )
        );
        final var backlogWithWrongDate = entityManager.persistFlushFind(
            new BacklogData(
                expectedUser,
                startDate.plusDays(1),
                duration
            )
        );
        final var backlogWithWrongDuration = entityManager.persistFlushFind(
            new BacklogData(
                expectedUser,
                startDate,
                BacklogData.Duration.WEEK
            )
        );


        final var backlogWithWrongUserId = entityManager.persistFlushFind(
            new BacklogData(
                wrongUser,
                startDate,
                BacklogData.Duration.WEEK
            )
        );

        // When
        final var result = jpaBacklogRepository.findByOwnerIdAndStartDateAndDuration(
            expectedUser.id(),
            startDate,
            duration
        );

        // Then
        assertThat(result).isEqualTo(expectedBacklog);
        assertThat(result).isNotIn(
            backlogWithWrongDate,
            backlogWithWrongDuration,
            backlogWithWrongUserId
        );
    }
}
