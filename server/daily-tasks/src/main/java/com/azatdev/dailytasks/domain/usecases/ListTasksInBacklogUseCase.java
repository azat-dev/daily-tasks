package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.List;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

public interface ListTasksInBacklogUseCase {

    public enum Error {
        INTERNAL_ERROR
    }

    public Result<List<Task>, Error> execute(
        LocalDate date,
        Backlog.Duration duration
    );
}
