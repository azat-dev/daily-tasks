package com.azatdev.dailytasks.domain.interfaces.dao;

import java.util.UUID;

import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
public interface UpdateTaskStatusDao {

    void execute(
        UUID userId,
        Long taskId,
        Task.Status newStatus
    );
}