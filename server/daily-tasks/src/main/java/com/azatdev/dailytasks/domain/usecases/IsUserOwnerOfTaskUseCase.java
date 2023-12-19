package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;

public final class IsUserOwnerOfTaskUseCase {

    private final GetTaskDao taskDao;

    public IsUserOwnerOfTaskUseCase(GetTaskDao taskDao) {
        this.taskDao = taskDao;
    }

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
