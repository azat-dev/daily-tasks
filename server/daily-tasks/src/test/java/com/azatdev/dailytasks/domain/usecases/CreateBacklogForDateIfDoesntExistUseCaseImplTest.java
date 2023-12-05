package com.azatdev.dailytasks.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;

public class CreateBacklogForDateIfDoesntExistUseCaseImplTest {

    private record SUT(
        CreateBacklogForDateIfDoesntExistUseCase useCase,
        AdjustDateToStartOfBacklog adjustDate,
        BacklogRepositoryGet backlogRepository
    ) {

    }

    private SUT createSUT() {
        final var adjustDate = mock(AdjustDateToStartOfBacklog.class);
        final var backlogRepository = mock(BacklogRepositoryGet.class);

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
    void execute_givenBacklogDoesntExist_thenCreateNewBacklog() {

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

        given(
            sut.backlogRepository.getBacklogId(
                any(),
                eq(Backlog.Duration.WEEK)
            )
        ).willReturn(Optional.empty());

        given(
            sut.adjustDate.calculate(
                wednesday,
                backlogDuration
            )
        ).willReturn(adjustedDate);

        // When
        assertDoesNotThrow(
            () -> sut.useCase.execute(
                wednesday,
                backlogDuration,
                Optional.empty()
            )
        );

        // Then
        then(sut.useCase).should(times(1))
            .execute(
                adjustedDate,
                Backlog.Duration.WEEK,
                Optional.empty()
            );
    }

}
