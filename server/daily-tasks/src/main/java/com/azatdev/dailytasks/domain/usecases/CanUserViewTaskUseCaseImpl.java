package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;

public final class CanUserViewTaskUseCaseImpl implements CanUserViewTaskUseCase {

    private final GetTaskDao taskDao;

    public CanUserViewTaskUseCaseImpl(GetTaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public boolean execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException {

        final var taskData = taskDao.execute(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));

        return taskData.ownerId()
            .equals(userId);
    }
}