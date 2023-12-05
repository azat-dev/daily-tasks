package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.Optional;

import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

public class CreateTaskInBacklogUseCaseImpl implements CreateTaskInBacklogUseCase {

    private final CreateBacklogForDateIfDoesntExistUseCase createBacklogIfDoesntExistUseCase;
    private final TasksRepositoryCreate tasksRepository;
    private final TransactionFactory transactionFactory;

    public CreateTaskInBacklogUseCaseImpl(
        CreateBacklogForDateIfDoesntExistUseCase createBacklogIfDoesntExistUseCase,
        TasksRepositoryCreate tasksRepository,
        TransactionFactory transactionFactory
    ) {
        this.createBacklogIfDoesntExistUseCase = createBacklogIfDoesntExistUseCase;
        this.tasksRepository = tasksRepository;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public Result<Task, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration,
        NewTaskData newTaskData
    ) {

        final var transaction = transactionFactory.make();

        try {
            transaction.begin();

            final var backlogId = createBacklogIfDoesntExistUseCase.execute(
                date,
                backlogDuration,
                Optional.of(transaction)
            );

            final var creationResult = tasksRepository.createTask(
                backlogId,
                newTaskData,
                transaction
            );

            if (!creationResult.isSuccess()) {
                transaction.rollback();
                return Result.failure(Error.INTERNAL_ERROR);
            }

            transaction.commit();
            return Result.success(creationResult.getValue());

        } catch (Exception e) {

            transaction.rollback();
            return Result.failure(Error.INTERNAL_ERROR);
        }
    }
}
