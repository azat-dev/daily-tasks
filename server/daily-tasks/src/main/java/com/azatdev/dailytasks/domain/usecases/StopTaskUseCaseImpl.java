package com.azatdev.dailytasks.domain.usecases;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.TaskAlreadyStoppedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.MarkTaskAsStoppedDao;
import com.azatdev.dailytasks.domain.interfaces.dao.StopActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.interfaces.utils.CurrentTimeProvider;

public final class StopTaskUseCaseImpl implements StopTaskUseCase {

    private final CurrentTimeProvider currentTimeProvider;
    private final GetRunningActivitySessionForTaskDao getCurrentRunningActivitySessionForTaskDao;
    private final StopActivitySessionDao stopActivitySessionDao;
    private final MarkTaskAsStoppedDao markTaskAsStoppedDao;

    private final TransactionFactory transactionFactory;

    public StopTaskUseCaseImpl(
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
    ) throws TaskNotFoundException, TaskAlreadyStoppedException {

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
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
