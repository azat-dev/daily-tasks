package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;

@FunctionalInterface
public interface DeleteTaskUseCase {
    void execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException;
}
