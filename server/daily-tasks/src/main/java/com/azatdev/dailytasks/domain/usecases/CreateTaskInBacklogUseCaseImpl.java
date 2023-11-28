package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;

import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryCreate;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

public class CreateTaskInBacklogUseCaseImpl implements CreateTaskInBacklogUseCase {

    private final CreateBacklogForDateIfDoesntExistUseCase createBacklogIfDoesntExistUseCase;
    private final TasksRepositoryCreate tasksRepository;

    public CreateTaskInBacklogUseCaseImpl(
        CreateBacklogForDateIfDoesntExistUseCase createBacklogIfDoesntExistUseCase,
        TasksRepositoryCreate tasksRepository
    ) {
        this.createBacklogIfDoesntExistUseCase = createBacklogIfDoesntExistUseCase;
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Result<Task, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration,
        NewTaskData newTaskData
    ) {

        final var backlogIdResult = createBacklogIfDoesntExistUseCase.execute(
            date,
            backlogDuration
        );

        if (!backlogIdResult.isSuccess()) {
            return Result.failure(Error.INTERNAL_ERROR);
        }

        final var backlogId = backlogIdResult.getValue();

        final var creationResult = tasksRepository.createTask(
            backlogId,
            newTaskData
        );

        if (!creationResult.isSuccess()) {
            return Result.failure(Error.INTERNAL_ERROR);
        }

        return Result.success(creationResult.getValue());
    }
}
