package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.exceptions.TaskAlreadyStoppedException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.MarkTaskAsStoppedDao;
import com.azatdev.dailytasks.domain.interfaces.dao.StopActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.interfaces.utils.CurrentTimeProvider;
import com.azatdev.dailytasks.domain.models.ActivitySession;

class StopTaskUseCaseImplTest {
    private record SUT(
        StopTaskUseCase useCase,
        CurrentTimeProvider currentTimeProvider,
        GetRunningActivitySessionForTaskDao getCurrentRunningActivitySessionForTaskDao,
        StopActivitySessionDao stopActivitySessionDao,
        MarkTaskAsStoppedDao markTaskAsStoppedDao,
        TransactionFactory transactionFactory,
        Transaction transaction
    ) {
    }

    private SUT createSUT(ZonedDateTime currentTime) {
        final var currentTimeProvider = mock(CurrentTimeProvider.class);
        given(currentTimeProvider.execute()).willReturn(currentTime);

        final var transactionFactory = mock(TransactionFactory.class);
        final var transaction = mock(Transaction.class);

        given(transactionFactory.make()).willReturn(transaction);

        final var getCurrentRunningActivitySessionDao = mock(GetRunningActivitySessionForTaskDao.class);
        final var stopActivitySessionDao = mock(StopActivitySessionDao.class);
        final var markTaskAsStoppedDao = mock(MarkTaskAsStoppedDao.class);

        final var useCase = new StopTaskUseCaseImpl(
            currentTimeProvider,
            getCurrentRunningActivitySessionDao,
            stopActivitySessionDao,
            markTaskAsStoppedDao,
            transactionFactory
        );

        return new SUT(
            useCase,
            currentTimeProvider,
            getCurrentRunningActivitySessionDao,
            stopActivitySessionDao,
            markTaskAsStoppedDao,
            transactionFactory,
            transaction
        );
    }

    UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenTaskExistsAndNotStarted_thenStopActivitySessionAndMarkTaskAsStopped() throws Exception {

        // Given
        final var userId = anyUserId();
        final var taskId = 1L;
        final var currentTime = ZonedDateTime.now();
        final var activeSessionId = 1L;

        final var sut = createSUT(currentTime);

        final var existingActivitySession = new ActivitySession(
            Optional.of(activeSessionId),
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
        final var stoppedAt = sut.useCase.execute(
            userId,
            taskId
        );

        // Then
        then(sut.transaction).should(times(1))
            .begin();

        then(sut.transaction).should(times(1))
            .commit();

        then(sut.markTaskAsStoppedDao).should(times(1))
            .execute(
                taskId,
                currentTime,
                Optional.of(sut.transaction)
            );

        then(sut.getCurrentRunningActivitySessionForTaskDao).should(times(1))
            .execute(
                userId,
                taskId
            );

        then(sut.stopActivitySessionDao).should(times(1))
            .execute(
                activeSessionId,
                currentTime,
                Optional.of(sut.transaction)
            );

        assertThat(stoppedAt).isEqualTo(currentTime);
    }

    @Test
    void execute_givenTaskExistsAndStopped_thenReturnError() throws Exception {

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
        final var exception = assertThrows(
            TaskAlreadyStoppedException.class,
            () -> sut.useCase.execute(
                userId,
                taskId
            )
        );

        // Then
        then(sut.transaction).should(times(1))
            .begin();

        then(sut.transaction).should(times(1))
            .commit();

        then(sut.getCurrentRunningActivitySessionForTaskDao).should(times(1))
            .execute(
                userId,
                taskId
            );

        then(sut.markTaskAsStoppedDao).should(never())
            .execute(
                anyLong(),
                any(),
                any()
            );

        then(sut.stopActivitySessionDao).should(never())
            .execute(
                anyLong(),
                any(),
                any()
            );

        then(sut.getCurrentRunningActivitySessionForTaskDao).should(times(1))
            .execute(
                userId,
                taskId
            );

        assertThat(exception.getTaskId()).isEqualTo(taskId);
    }

    @Test
    void execute_givenExceptionDuringExecution_thenRollback() throws Exception {

        // Given
        final var userId = anyUserId();
        final var taskId = 1L;
        final var activeSessionId = 222L;
        final var currentTime = ZonedDateTime.now();

        final var sut = createSUT(currentTime);

        final var existingActivitySession = new ActivitySession(
            Optional.of(activeSessionId),
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

        given(
            sut.markTaskAsStoppedDao.execute(
                anyLong(),
                any(),
                any()
            )
        ).willThrow(new RuntimeException());

        // When
        assertThrows(
            RuntimeException.class,
            () -> sut.useCase.execute(
                userId,
                taskId
            )
        );

        // Then
        then(sut.transaction).should(times(1))
            .begin();

        then(sut.transaction).should(never())
            .commit();

        then(sut.transaction).should(times(1))
            .rollback();
    }
}
