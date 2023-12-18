package com.azatdev.dailytasks.domain.usecases;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;

@FunctionalInterface
public interface StartTaskUseCase {
    // Methods
    ZonedDateTime execute(
        UUID userId,
        Long taskId
    ) throws TaskNotFoundException;
}
