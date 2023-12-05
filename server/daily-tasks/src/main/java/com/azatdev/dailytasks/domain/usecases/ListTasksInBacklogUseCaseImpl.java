package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.List;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;

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
    public List<Task> execute(
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
            return List.of();
        }

        final var tasksInBacklog = tasksRepository.list(backlogIdResult.get());
        return tasksInBacklog;
    }
}
