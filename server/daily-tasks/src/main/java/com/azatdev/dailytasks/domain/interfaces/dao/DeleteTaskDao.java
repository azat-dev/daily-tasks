package com.azatdev.dailytasks.domain.interfaces.dao;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;

@FunctionalInterface
public interface DeleteTaskDao {
    void execute(long taskId) throws TaskNotFoundException;
}
