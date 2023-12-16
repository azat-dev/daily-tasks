package com.azatdev.dailytasks.domain.usecases;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.interfaces.dao.AddNewActivitySessionDao;
import com.azatdev.dailytasks.domain.interfaces.dao.GetRunningActivitySessionForTaskDao;
import com.azatdev.dailytasks.domain.interfaces.dao.UpdateTaskStatusDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.interfaces.utils.CurrentTimeProvider;
import com.azatdev.dailytasks.domain.models.NewActivitySession;
import com.azatdev.dailytasks.domain.models.Task;

public final class StartTaskUseCaseImpl implements StartTaskUseCase {

    private CurrentTimeProvider currentTimeProvider;
    private GetRunningActivitySessionForTaskDao getCurrentActivitySessionDao;
    private AddNewActivitySessionDao addNewActivitySessionDao;
    private UpdateTaskStatusDao updateTaskStatusDao;
    private TransactionFactory transactionFactory;

    public StartTaskUseCaseImpl(
        CurrentTimeProvider currentTimeProvider,
        GetRunningActivitySessionForTaskDao getCurrentActivitySessionDao,
        AddNewActivitySessionDao addNewActivitySessionDao,
        UpdateTaskStatusDao updateTaskStatusDao,
        TransactionFactory transactionFactory
    ) {
        this.currentTimeProvider = currentTimeProvider;
        this.getCurrentActivitySessionDao = getCurrentActivitySessionDao;
        this.addNewActivitySessionDao = addNewActivitySessionDao;
        this.updateTaskStatusDao = updateTaskStatusDao;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public ZonedDateTime execute(
        UUID userId,
        Long taskId
    ) {

        final var transaction = transactionFactory.make();

        try {
            transaction.begin();
            
            final var currentActivitySession = getCurrentActivitySessionDao.execute(
                userId,
                taskId
            );

            if (currentActivitySession.isPresent()) {
                transaction.commit();
                return currentActivitySession.get()
                    .startedAt();
            }

            final var currentTime = currentTimeProvider.execute();

            updateTaskStatusDao.execute(
                userId,
                taskId,
                Task.Status.IN_PROGRESS
            );

            addNewActivitySessionDao.execute(
                new NewActivitySession(
                    userId,
                    taskId,
                    currentTime,
                    Optional.empty()
                )
            );

            transaction.commit();

            return currentTime;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
