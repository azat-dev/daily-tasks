package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.DeleteTaskDao;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;

public final class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {

    private final CanUserDeleteTaskUseCase canUserDeleteTaskUseCase;
    private final StopTaskUseCase stopTaskUseCase;
    private final DeleteTaskDao deleteTaskDao;
    private final TransactionFactory transactionFactory;

    public DeleteTaskUseCaseImpl(
        CanUserDeleteTaskUseCase canUserDeleteTaskUseCase,
        StopTaskUseCase stopTaskUseCase,
        DeleteTaskDao deleteTaskDao,
        TransactionFactory transactionFactory
    ) {
        this.canUserDeleteTaskUseCase = canUserDeleteTaskUseCase;
        this.stopTaskUseCase = stopTaskUseCase;
        this.deleteTaskDao = deleteTaskDao;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException, AccessDeniedException {

        final var transaction = transactionFactory.make();

        try {
            transaction.begin();
            final boolean hasPermission = canUserDeleteTaskUseCase.execute(
                userId,
                taskId
            );

            if (!hasPermission) {
                throw new AccessDeniedException(
                    userId,
                    "deleteTask",
                    String.valueOf(taskId)
                );
            }

            stopTaskUseCase.execute(
                null,
                taskId
            );

            deleteTaskDao.execute(taskId);
            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
