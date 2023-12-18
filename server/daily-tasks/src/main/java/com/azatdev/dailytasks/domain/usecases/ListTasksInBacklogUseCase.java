package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;

public interface ListTasksInBacklogUseCase {

    List<Task> execute(
        UUID ownerId,
        LocalDate date,
        Backlog.Duration duration
    ) throws AccessDeniedException;
}
