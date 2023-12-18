package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;

@FunctionalInterface
public interface CanUserDeleteTaskUseCase {
    boolean execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException, AccessDeniedException;
}
