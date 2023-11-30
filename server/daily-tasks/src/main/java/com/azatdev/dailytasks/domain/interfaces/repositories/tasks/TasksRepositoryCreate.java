package com.azatdev.dailytasks.domain.interfaces.repositories.tasks;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

public interface TasksRepositoryCreate {

    public enum Error {
        INTERNAL_ERROR,
        BACKLOG_NOT_FOUND
    }

    public Result<Task, Error> createTask(
        Long backlogId,
        Integer backlogOrder,
        NewTaskData newTaskData,
        Transaction transaction
    );
}
