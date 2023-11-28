package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

public interface CreateTaskInBacklogUseCase {

    public enum Error {
        INTERNAL_ERROR
    }

    public Result<Task, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration,
        NewTaskData newTaskData
    );
}
