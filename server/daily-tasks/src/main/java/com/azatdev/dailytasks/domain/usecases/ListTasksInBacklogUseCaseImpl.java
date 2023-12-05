package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;
import com.azatdev.dailytasks.utils.Result;

public final class ListTasksInBacklogUseCaseImpl implements ListTasksInBacklogUseCase {

    private final BacklogRepositoryGet backlogRepository;
    private final TasksRepositoryList tasksRepository;
    private final AdjustDateToStartOfBacklog adjustDateToStart;

    public ListTasksInBacklogUseCaseImpl(
        BacklogRepositoryGet backlogRepository,
        TasksRepositoryList tasksRepository,
        AdjustDateToStartOfBacklog adjustBacklogTime
    ) {
        this.backlogRepository = backlogRepository;
        this.tasksRepository = tasksRepository;
        this.adjustDateToStart = adjustBacklogTime;
    }

    @Override
    public Result<List<Task>, Error> execute(
        LocalDate forDate,
        Backlog.Duration duration
    ) {
        final var backlogStartDate = adjustDateToStart.calculate(
            forDate,
            duration
        );

        final var backlogIdResult = backlogRepository.getBacklogId(
            backlogStartDate,
            duration
        );

        if (backlogIdResult.isEmpty()) {
            return Result.success(new ArrayList<>());
        }

        final var listTasksResult = tasksRepository.list(backlogIdResult.get());

        return Result.success(listTasksResult);
    }
}
