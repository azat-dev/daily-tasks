package com.azatdev.dailytasks.domain.interfaces.dao;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
public interface GetTaskDao {

    Optional<Task> execute(
        UUID userId,
        long taskId
    );
}
