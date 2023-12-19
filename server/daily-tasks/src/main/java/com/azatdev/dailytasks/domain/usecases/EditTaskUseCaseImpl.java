package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.data.dao.task.UpdateTaskDao;
import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.models.EditTaskData;

public final class EditTaskUseCaseImpl implements EditTaskUseCase {

    private final CanUserEditTaskUseCase canUserEditTaskUseCase;
    private final UpdateTaskDao updateTaskDao;
    private final TransactionFactory transactionFactory;

    public EditTaskUseCaseImpl(
        CanUserEditTaskUseCase canUserEditTaskUseCase,
        UpdateTaskDao updateTaskDao,
        TransactionFactory transactionFactory
    ) {
        this.canUserEditTaskUseCase = canUserEditTaskUseCase;
        this.updateTaskDao = updateTaskDao;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void execute(
        UUID userId,
        long taskId,
        EditTaskData data
    ) throws AccessDeniedException, TaskNotFoundException {

        final var canUserEdit = canUserEditTaskUseCase.execute(
            userId,
            taskId
        );

        if (!canUserEdit) {
            throw new AccessDeniedException(
                userId,
                "editTask",
                String.valueOf(taskId)
            );
        }

        final var transaction = transactionFactory.make();

        try {
            transaction.begin();

            updateTaskDao.execute(
                taskId,
                data
            );

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
