package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;

public final class CanUserDeleteTaskUseCaseImpl implements CanUserDeleteTaskUseCase {

    private final GetTaskDao getTaskDao;

    public CanUserDeleteTaskUseCaseImpl(GetTaskDao getTaskDao) {
        this.getTaskDao = getTaskDao;
    }

    @Override
    public boolean execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException, AccessDeniedException {

        final var taskData = getTaskDao.execute(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));

        return taskData.ownerId()
            .equals(userId);
    }
}