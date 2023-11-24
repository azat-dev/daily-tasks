package com.azatdev.dailytasks.data.repositories;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import com.azatdev.dailytasks.data.repositories.persistence.backlog.BacklogRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPABacklogRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.models.Backlog;

class BacklogRepositoryImplTests {
    private record SUT(
        BacklogRepositoryGet backlogRepository,
        JPABacklogRepository jpaBacklogRepository
    ) {

    }

    private SUT createSUT() {

        JPABacklogRepository jpaBacklogRepository = mock(JPABacklogRepository.class);

        return new SUT(
            new BacklogRepositoryImpl(jpaBacklogRepository),
            jpaBacklogRepository
        );
    }

    @Test
    void getBacklogIdEmptyDbShouldReturnEmptyOptionalTest() {
        
        // Given
        final var startDate = LocalDate.now();
        final var duration = Backlog.Duration.DAY;
        final var expected = Optional.empty();


        final var sut = createSUT();

        // When
        var result = sut.backlogRepository.getBacklogId(startDate, duration);

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(expected);
    }
}