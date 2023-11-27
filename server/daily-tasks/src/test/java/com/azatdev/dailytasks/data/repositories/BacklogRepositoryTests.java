package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.azatdev.dailytasks.data.repositories.persistence.backlog.BacklogRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPABacklogRepository;
import com.azatdev.dailytasks.domain.models.Backlog;

@ExtendWith(MockitoExtension.class)
class BacklogRepositoryTests {

    @Mock
    JPABacklogRepository jpaBacklogRepository;

    @InjectMocks
    BacklogRepositoryImpl backlogRepository;

    @Test
    void getBacklogIdEmptyDbShouldReturnEmptyOptionalTest() {

        // Given
        final var startDate = LocalDate.now();
        final var duration = Backlog.Duration.DAY;
        final BacklogData expectedBacklogData = null;

        given(
            jpaBacklogRepository.findByStartDateAndDuration(
                any(LocalDate.class),
                any(BacklogData.Duration.class)
            )
        ).willReturn(expectedBacklogData);

        // When
        var result = backlogRepository.getBacklogId(
            startDate,
            duration
        );

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEmpty();

        then(jpaBacklogRepository).should(times(1))
            .findByStartDateAndDuration(
                startDate,
                BacklogData.Duration.DAY
            );
    }
}
