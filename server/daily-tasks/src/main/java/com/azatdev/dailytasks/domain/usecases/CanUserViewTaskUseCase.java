package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;

public interface CanUserViewTaskUseCase {

    boolean execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException;
}