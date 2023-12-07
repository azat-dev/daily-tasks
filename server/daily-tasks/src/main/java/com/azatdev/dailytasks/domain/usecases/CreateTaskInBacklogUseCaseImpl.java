package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;

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
    public Task execute(
        UUID ownerId,
        LocalDate date,
        Backlog.Duration backlogDuration,
        NewTaskData newTaskData
    ) {

        final var transaction = transactionFactory.make();

        try {
            transaction.begin();

            final var backlogId = createBacklogIfDoesntExistUseCase.execute(
                ownerId,
                date,
                backlogDuration,
                Optional.of(transaction)
            );

            final var createdTask = tasksRepository.createTask(
                ownerId,
                backlogId,
                newTaskData,
                Optional.of(transaction)
            );

            transaction.commit();
            return createdTask;

        } catch (Exception e) {

            transaction.rollback();
            throw new RuntimeException(e);
        }
    }
}
