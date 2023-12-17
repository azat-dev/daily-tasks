package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.interfaces.utils.CurrentTimeProvider;
import com.azatdev.dailytasks.domain.models.ActivitySession;

@FunctionalInterface
interface StopTaskUseCase {
    ZonedDateTime execute(
        UUID userId,
        long taskId
    ) throws RuntimeException;

    class TaskAlreadyStoppedException extends RuntimeException {

        private long taskId;

        public TaskAlreadyStoppedException(long taskId) {
            super("Task with id " + taskId + " is already stopped");
            this.taskId = taskId;
        }

        long getTaskId() {
            return taskId;
        }
    }
}

@FunctionalInterface
interface StopActivitySessionDao {

    void execute(
        long activitySessionId,
        ZonedDateTime finishedAt,
        Optional<Transaction> transaction
    );
}

@FunctionalInterface
interface MarkTaskAsStoppedDao {

    Void execute(
        long taskId,
        ZonedDateTime finishedAt,
        Optional<Transaction> transaction
    );
}

final class StopTaskUseCaseImpl implements StopTaskUseCase {

    private final CurrentTimeProvider currentTimeProvider;
    private final GetRunningActivitySessionForTaskDao getCurrentRunningActivitySessionForTaskDao;
    private final StopActivitySessionDao stopActivitySessionDao;
    private final MarkTaskAsStoppedDao markTaskAsStoppedDao;

    private final TransactionFactory transactionFactory;

    StopTaskUseCaseImpl(
        CurrentTimeProvider currentTimeProvider,
        GetRunningActivitySessionForTaskDao getCurrentRunningActivitySessionForTaskDao,
        StopActivitySessionDao stopActivitySessionDao,
        MarkTaskAsStoppedDao markTaskAsStoppedDao,
        TransactionFactory transactionFactory
    ) {
        this.currentTimeProvider = currentTimeProvider;
        this.getCurrentRunningActivitySessionForTaskDao = getCurrentRunningActivitySessionForTaskDao;
        this.stopActivitySessionDao = stopActivitySessionDao;
        this.markTaskAsStoppedDao = markTaskAsStoppedDao;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public ZonedDateTime execute(
        UUID userId,
        long taskId
    ) {

        final var currentTime = currentTimeProvider.execute();
        final var transaction = transactionFactory.make();

        try {
            transaction.begin();

            final var existingActivitySessionResult = getCurrentRunningActivitySessionForTaskDao.execute(
                userId,
                taskId
            );

            if (existingActivitySessionResult.isEmpty()) {
                transaction.commit();
                throw new TaskAlreadyStoppedException(taskId);
            }

            final var existingActivitySession = existingActivitySessionResult.get();

            stopActivitySessionDao.execute(
                existingActivitySession.id()
                    .get(),
                currentTime,
                Optional.of(transaction)
            );

            markTaskAsStoppedDao.execute(
                taskId,
                currentTime,
                Optional.of(transaction)
            );

            transaction.commit();
            return currentTime;
        } catch (TaskAlreadyStoppedException e) {
            throw e;
        }catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}

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
    void execute_givenTaskExistsAndNotStarted_thenStopActivitySessionAndMarkTaskAsStopped() {

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
    void execute_givenTaskExistsAndStopped_thenReturnError() {

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
            StopTaskUseCase.TaskAlreadyStoppedException.class,
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
    void execute_givenExceptionDuringExecution_thenRollback() {

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
