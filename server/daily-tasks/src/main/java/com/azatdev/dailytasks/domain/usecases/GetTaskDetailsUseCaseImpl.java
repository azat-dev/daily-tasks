package com.azatdev.dailytasks.domain.usecases;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;
import com.azatdev.dailytasks.domain.models.Task;

public final class GetTaskDetailsUseCaseImpl implements GetTaskDetailsUseCase {

    private GetTaskDao getTaskDao;

    public GetTaskDetailsUseCaseImpl(GetTaskDao getTaskDao) {
        this.getTaskDao = getTaskDao;
    }

    @Override
    public Optional<Task> execute(
        UUID userId,
        long taskId
    ) throws AccessDeniedException, TaskNotFoundException {

        final var taskResult = getTaskDao.execute(taskId);

        if (taskResult.isEmpty()) {
            throw new TaskNotFoundException(taskId);
        }

        final var task = taskResult.get();
        if (task.ownerId() == userId) {
            return taskResult;
        }

        throw new AccessDeniedException(
            userId,
            "getTaskDetails",
            String.valueOf(taskId)
        );
    }
}
