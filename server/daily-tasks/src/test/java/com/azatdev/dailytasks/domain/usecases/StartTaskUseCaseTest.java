package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

@FunctionalInterface
interface StartTaskUseCase {
    // Methods
    ZonedDateTime execute(
        UUID userId,
        Long taskId
    );
}

final class StartTaskUseCaseImpl implements StartTaskUseCase {

    private CurrentTimeProvider currentTimeProvider;
    private GetCurrentActivitySessionDao getCurrentActivitySessionDao;
    private AddNewActivitySessionDao addNewActivitySessionDao;

    public StartTaskUseCaseImpl(
        CurrentTimeProvider currentTimeProvider,
        GetCurrentActivitySessionDao getCurrentActivitySessionDao,
        AddNewActivitySessionDao addNewActivitySessionDao
    ) {
        this.currentTimeProvider = currentTimeProvider;
        this.getCurrentActivitySessionDao = getCurrentActivitySessionDao;
        this.addNewActivitySessionDao = addNewActivitySessionDao;
    }

    @Override
    public ZonedDateTime execute(
        UUID userId,
        Long taskId
    ) {

        final var currentActivitySession = getCurrentActivitySessionDao.execute(
            userId,
            taskId
        );

        if (currentActivitySession.isPresent()) {
            throw new UnsupportedOperationException("Unimplemented method 'execute'");
        }

        final var currentTime = currentTimeProvider.execute();
        addNewActivitySessionDao.execute(
            userId,
            taskId,
            currentTime,
            Optional.empty()
        );

        return currentTime;
    }

}

record ActivitySession(
    Optional<Long> id,
    UUID userId,
    long taskId,
    ZonedDateTime startedAt,
    Optional<ZonedDateTime> finishedAt
) {
}

@FunctionalInterface
interface GetCurrentActivitySessionDao {
    Optional<ActivitySession> execute(
        UUID userId,
        long taskId
    );
}

@FunctionalInterface
interface AddNewActivitySessionDao {
    Optional<ActivitySession> execute(
        UUID userId,
        long taskId,
        ZonedDateTime startedAt,
        Optional<ZonedDateTime> finishedAt
    );
}

@FunctionalInterface
interface CurrentTimeProvider {
    ZonedDateTime execute();
}

class StartTaskUseCaseTest {

    private record SUT(
        StartTaskUseCase useCase,
        CurrentTimeProvider currentTimeProvider,
        GetCurrentActivitySessionDao getCurrentActivitySessionDao,
        AddNewActivitySessionDao addNewActivitySessionDao
    ) {
    }

    private SUT createSUT(ZonedDateTime currentTime) {
        final var currentTimeProvider = mock(CurrentTimeProvider.class);
        given(currentTimeProvider.execute()).willReturn(currentTime);

        final var getCurrentActivitySessionDao = mock(GetCurrentActivitySessionDao.class);
        final var addNewActivitySessionDao = mock(AddNewActivitySessionDao.class);

        final var useCase = new StartTaskUseCaseImpl(
            currentTimeProvider,
            getCurrentActivitySessionDao,
            addNewActivitySessionDao
        );

        return new SUT(
            useCase,
            currentTimeProvider,
            getCurrentActivitySessionDao,
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
            sut.getCurrentActivitySessionDao.execute(
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
        then(sut.getCurrentActivitySessionDao).should(times(1))
            .execute(
                userId,
                taskId
            );

        then(sut.addNewActivitySessionDao).should(times(1))
            .execute(
                userId,
                taskId,
                startedAt,
                Optional.empty()
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
            sut.getCurrentActivitySessionDao.execute(
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
        then(sut.getCurrentActivitySessionDao).should(times(1))
            .execute(
                userId,
                taskId
            );

        then(sut.addNewActivitySessionDao).should(never())
            .execute(
                any(),
                any(),
                any(),
                any()
            );

        assertThat(startedAt).isEqualTo(existingActivitySession.startedAt());
    }
}
