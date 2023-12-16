package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.interfaces.utils.CurrentTimeProvider;
import com.azatdev.dailytasks.domain.models.ActivitySession;
import com.azatdev.dailytasks.domain.models.NewActivitySession;

class StartTaskUseCaseImplTest {

    private record SUT(
        StartTaskUseCase useCase,
        CurrentTimeProvider currentTimeProvider,
        GetRunningActivitySessionForTaskDao getCurrentRunningActivitySessionForTaskDao,
        AddNewActivitySessionDao addNewActivitySessionDao
    ) {
    }

    private SUT createSUT(ZonedDateTime currentTime) {
        final var currentTimeProvider = mock(CurrentTimeProvider.class);
        given(currentTimeProvider.execute()).willReturn(currentTime);

        final var getCurrentRunningActivitySessionDao = mock(GetRunningActivitySessionForTaskDao.class);
        final var addNewActivitySessionDao = mock(AddNewActivitySessionDao.class);

        final var useCase = new StartTaskUseCaseImpl(
            currentTimeProvider,
            getCurrentRunningActivitySessionDao,
            addNewActivitySessionDao
        );

        return new SUT(
            useCase,
            currentTimeProvider,
            getCurrentRunningActivitySessionDao,
            addNewActivitySessionDao
        );
    }

    UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenTaskExistsAndNotStarted_thenCreateNewActivitySession() {

        // Given
        final var userId = anyUserId();
        final var taskId = 1L;
        final var currentTime = ZonedDateTime.now();

        final var sut = createSUT(currentTime);

        given(
            sut.getCurrentRunningActivitySessionForTaskDao.execute(
                userId,
                taskId
            )
        ).willReturn(Optional.empty());

        // When
        final var startedAt = sut.useCase.execute(
            userId,
            taskId
        );

        // Then
        then(sut.getCurrentRunningActivitySessionForTaskDao).should(times(1))
            .execute(
                userId,
                taskId
            );

        then(sut.addNewActivitySessionDao).should(times(1))
            .execute(
                new NewActivitySession(
                    userId,
                    taskId,
                    startedAt,
                    Optional.empty()
                )
            );

        assertThat(startedAt).isEqualTo(currentTime);
    }

    @Test
    void execute_givenTaskExistsAnStarted_thenReturnStartTimeFromExistingActivitySession() {

        // Given
        final var userId = anyUserId();
        final var taskId = 1L;
        final var currentTime = ZonedDateTime.now();

        final var sut = createSUT(currentTime);

        final var existingActivitySession = new ActivitySession(
            Optional.of(1L),
            userId,
            taskId,
            currentTime.minusDays(2),
            Optional.empty()
        );

        given(
            sut.getCurrentRunningActivitySessionForTaskDao.execute(
                userId,
                taskId
            )
        ).willReturn(Optional.of(existingActivitySession));

        // When
        final var startedAt = sut.useCase.execute(
            userId,
            taskId
        );

        // Then
        then(sut.getCurrentRunningActivitySessionForTaskDao).should(times(1))
            .execute(
                userId,
                taskId
            );

        then(sut.addNewActivitySessionDao).should(never())
            .execute(any());

        assertThat(startedAt).isEqualTo(existingActivitySession.startedAt());
    }
}
