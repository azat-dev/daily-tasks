package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;
import com.azatdev.dailytasks.utils.Result;

public class ListTasksInBacklogUseCaseImpl implements ListTasksInBacklogUseCase {
    
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
    public Result<Task[], Error> execute(LocalDate forDate, Backlog.Duration duration) {
        var backlogStartDate = adjustDateToStart.calculate(forDate, duration);
        var getBacklogIdResult = backlogRepository.getBacklogId(backlogStartDate, duration);

        if (!getBacklogIdResult.isSuccess()) {
            return Result.success(new Task[]{});
        }

        final var optionalBacklogId = getBacklogIdResult.getValue();

        if (optionalBacklogId.isEmpty()) {
            return Result.success(new Task[]{});
        }

        var listTasksResult = tasksRepository.list(optionalBacklogId.get());

        if (listTasksResult.isSuccess()) {
            return Result.success(listTasksResult.getValue());
        }

        return Result.failure(Error.INTERNAL_ERROR);
    }
}