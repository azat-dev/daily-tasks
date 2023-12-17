package com.azatdev.dailytasks.domain.usecases;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
public interface GetTaskDetailsUseCase {

    Optional<Task> execute(
        UUID userId,
        long taskId
    );
}
