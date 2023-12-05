package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.List;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;

public interface ListTasksInBacklogUseCase {

    List<Task> execute(
        LocalDate date,
        Backlog.Duration duration
    );
}
