package com.azatdev.dailytasks.data.dao.task;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.models.EditTaskData;

@FunctionalInterface
public interface UpdateTaskDao {

    void execute(
        long taskId,
        EditTaskData data
    ) throws TaskNotFoundException;
}
