package com.azatdev.dailytasks.domain.interfaces.dao;

import java.util.Optional;

import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
public interface GetTaskDao {

    Optional<Task> execute(long taskId);
}
