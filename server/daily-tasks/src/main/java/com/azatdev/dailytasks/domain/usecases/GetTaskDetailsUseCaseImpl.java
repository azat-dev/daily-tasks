package com.azatdev.dailytasks.domain.usecases;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;
import com.azatdev.dailytasks.domain.models.Task;

public final class GetTaskDetailsUseCaseImpl implements GetTaskDetailsUseCase {

    private final CanUserViewTaskUseCase canUserViewTaskUseCase;
    private GetTaskDao getTaskDao;

    public GetTaskDetailsUseCaseImpl(
        CanUserViewTaskUseCase canUserViewTaskUseCase,
        GetTaskDao getTaskDao
    ) {
        this.canUserViewTaskUseCase = canUserViewTaskUseCase;
        this.getTaskDao = getTaskDao;
    }

    @Override
    public Optional<Task> execute(
        UUID userId,
        long taskId
    ) throws AccessDeniedException {

        try {
            final var canUserViewTask = canUserViewTaskUseCase.execute(
                userId,
                taskId
            );

            if (!canUserViewTask) {
                throw new AccessDeniedException(
                    userId,
                    "getTaskDetails",
                    String.valueOf(taskId)
                );
            }

        } catch (TaskNotFoundException e) {
            return Optional.empty();
        }

        return getTaskDao.execute(taskId);
    }
}
