package com.azatdev.dailytasks.domain.usecases;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskAlreadyStoppedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;

@FunctionalInterface
public interface StopTaskUseCase {
    ZonedDateTime execute(
        UUID userId,
        long taskId
    ) throws TaskNotFoundException, TaskAlreadyStoppedException, AccessDeniedException;
}
