package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.BacklogNotFoundException;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;

public final class ListTasksInBacklogUseCaseImpl implements ListTasksInBacklogUseCase {

    private final CanUserViewBacklogUseCase canUserViewBacklogUseCase;
    private final BacklogRepositoryGet backlogRepository;
    private final TasksRepositoryList tasksRepository;
    private final AdjustDateToStartOfBacklog adjustDateToStart;

    public ListTasksInBacklogUseCaseImpl(
        CanUserViewBacklogUseCase canUserViewBacklogUseCase,
        BacklogRepositoryGet backlogRepository,
        TasksRepositoryList tasksRepository,
        AdjustDateToStartOfBacklog adjustBacklogTime
    ) {
        this.canUserViewBacklogUseCase = canUserViewBacklogUseCase;
        this.backlogRepository = backlogRepository;
        this.tasksRepository = tasksRepository;
        this.adjustDateToStart = adjustBacklogTime;
    }

    @Override
    public List<Task> execute(
        UUID userId,
        LocalDate forDate,
        Backlog.Duration duration
    ) throws AccessDeniedException, BacklogNotFoundException {

        final var backlogStartDate = adjustDateToStart.calculate(
            forDate,
            duration
        );

        final var backlogIdResult = backlogRepository.getBacklogId(
            userId,
            backlogStartDate,
            duration
        );

        if (backlogIdResult.isEmpty()) {
            return List.of();
        }

        final var backlogId = backlogIdResult.get();

        final var canView = canUserViewBacklogUseCase.execute(
            userId,
            backlogId
        );

        if (!canView) {
            throw new AccessDeniedException(
                userId,
                "viewBacklog",
                String.valueOf(backlogId)
            );
        }

        final var tasksInBacklog = tasksRepository.list(
            userId,
            backlogId
        );
        return tasksInBacklog;
    }
}
