package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.azatdev.dailytasks.data.dao.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaBacklogsRepository;
import com.azatdev.dailytasks.data.repositories.TestEntityDataGenerator;

@DataJpaTest
@Import(TestEntityDataGenerator.class)
class JpaBacklogRepositoryTests {

    @Autowired
    TestEntityDataGenerator testData;

    @Autowired
    private JpaBacklogsRepository jpaBacklogRepository;

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
        assertThat(result).isEmpty();
    }

    @Test
    void findByOwnerIdAndStartDateAndDuration_givenNotEmptyDb_thenShouldReturnCorrectRecord() {

        // Given
        final var expectedUser = testData.givenExistingUser("expectedUser");
        final var wrongUser = testData.givenExistingUser("wrongUser");

        final var expectedBacklog = testData.givenExistingWeekBacklog(expectedUser);

        final var startDate = expectedBacklog.getStartDate();
        final var duration = expectedBacklog.getDuration();

        final var backlogWithWrongDate = testData.givenExistingWeekBacklog(
            expectedUser,
            startDate.plusDays(1)
        );

        final var backlogWithWrongDuration = testData.givenExistingDayBacklog(
            expectedUser,
            startDate
        );

        final var backlogWithWrongUserId = testData.givenExistingWeekBacklog(
            wrongUser,
            startDate
        );

        // When
        final var foundBacklogIdProjection = jpaBacklogRepository.findByOwnerIdAndStartDateAndDuration(
            expectedUser.getId(),
            startDate,
            duration
        );

        // Then
        assertThat(foundBacklogIdProjection).isNotEmpty();
        final var foundBacklogId = foundBacklogIdProjection.get()
            .getId();

        assertThat(foundBacklogId).isEqualTo(expectedBacklog.getId());

        assertThat(foundBacklogId).isNotIn(
            backlogWithWrongDate.getId(),
            backlogWithWrongDuration.getId(),
            backlogWithWrongUserId.getId()
        );
    }
}
