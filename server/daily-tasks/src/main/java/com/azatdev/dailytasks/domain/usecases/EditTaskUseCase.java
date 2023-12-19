package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.models.EditTaskData;

@FunctionalInterface
public interface EditTaskUseCase {

    void execute(
        UUID userId,
        long taskId,
        EditTaskData data
    ) throws AccessDeniedException, TaskNotFoundException;
}
