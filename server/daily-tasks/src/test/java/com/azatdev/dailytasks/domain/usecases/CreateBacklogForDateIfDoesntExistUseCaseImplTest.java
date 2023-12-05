package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryCreate;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;

public class CreateBacklogForDateIfDoesntExistUseCaseImplTest {

    private record SUT(
        CreateBacklogForDateIfDoesntExistUseCase useCase,
        AdjustDateToStartOfBacklog adjustDate,
        BacklogRepositoryCreate backlogRepository
    ) {

    }

    private SUT createSUT() {
        final var adjustDate = mock(AdjustDateToStartOfBacklog.class);
        final var backlogRepository = mock(BacklogRepositoryCreate.class);

        return new SUT(
            new CreateBacklogForDateIfDoesntExistUseCaseImpl(
                adjustDate,
                backlogRepository
            ),
            adjustDate,
            backlogRepository
        );
    }

    @Test
    void execute_givenBacklogDoesntExist_thenCreateNewBacklog() throws Exception {

        // Given
        final var wednesday = LocalDate.of(
            2021,
            1,
            7
        );

        final var adjustedDate = LocalDate.of(
            2021,
            1,
            7
        );

        final var backlogDuration = Backlog.Duration.WEEK;

        final var sut = createSUT();

        final var backlogId = 1L;

        given(
            sut.backlogRepository.create(
                any(),
                eq(Backlog.Duration.WEEK),
                any()
            )
        ).willReturn(backlogId);

        given(
            sut.adjustDate.calculate(
                wednesday,
                backlogDuration
            )
        ).willReturn(adjustedDate);

        // When
        final var createdBacklogId = assertDoesNotThrow(
            () -> sut.useCase.execute(
                wednesday,
                backlogDuration,
                Optional.empty()
            )
        );

        // Then
        then(sut.backlogRepository).should(times(1))
            .create(
                adjustedDate,
                backlogDuration,
                Optional.empty()
            );

        assertThat(createdBacklogId).isEqualTo(backlogId);
    }

    @Test
    void execute_givenBacklogExists_thenThrowException() throws Exception {

        // Given
        final var wednesday = LocalDate.of(
            2021,
            1,
            7
        );

        final var adjustedDate = LocalDate.of(
            2021,
            1,
            7
        );

        final var backlogDuration = Backlog.Duration.WEEK;

        final var sut = createSUT();

        final var backlogId = 1L;

        given(
            sut.adjustDate.calculate(
                wednesday,
                backlogDuration
            )
        ).willReturn(adjustedDate);

        given(
            sut.backlogRepository.create(
                adjustedDate,
                Backlog.Duration.WEEK,
                Optional.empty()
            )
        ).willThrow(new BacklogRepositoryCreate.BacklogAlreadyExistsException(backlogId));

        // When
        final var createdBacklogId = assertDoesNotThrow(
            () -> sut.useCase.execute(
                wednesday,
                backlogDuration,
                Optional.empty()
            )
        );

        // Then

        assertThat(createdBacklogId).isEqualTo(backlogId);
    }
}
