package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.UUID;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;

public interface CreateTaskInBacklogUseCase {

    public Task execute(
        UUID ownerId,
        LocalDate date,
        Backlog.Duration backlogDuration,
        NewTaskData newTaskData
    );
}
