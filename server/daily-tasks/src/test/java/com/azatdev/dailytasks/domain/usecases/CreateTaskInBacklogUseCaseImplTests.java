package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;

class CreateTaskInBacklogUseCaseImplTests {

    private record SUT(
        CreateTaskInBacklogUseCase useCase,
        CreateBacklogForDateIfDoesntExistUseCase createBacklogIfDoesntExist,
        TasksRepositoryCreate tasksRepository,
        TransactionFactory transactionFactory,
        Transaction transaction
    ) {
    }

    private SUT createSUT() {

        final var transactionFactory = mock(TransactionFactory.class);

        final var transaction = mock(Transaction.class);

        given(transactionFactory.make()).willReturn(transaction);

        final var createBacklogIfDoesntExistUseCase = mock(CreateBacklogForDateIfDoesntExistUseCase.class);

        final var tasksRepository = mock(TasksRepositoryCreate.class);

        return new SUT(
            new CreateTaskInBacklogUseCaseImpl(
                createBacklogIfDoesntExistUseCase,
                tasksRepository,
                transactionFactory
            ),
            createBacklogIfDoesntExistUseCase,
            tasksRepository,
            transactionFactory,
            transaction
        );
    }

    private UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenNoExistingBacklog_thenShouldCreateBacklogCreateTaskAndReturnCreatedTask() throws Exception {

        // Given
        final var ownerId = anyUserId();
        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();

        final long backlogId = 1L;

        final var newTaskData = new NewTaskData(
            "title",
            Task.Priority.HIGH,
            "description"
        );

        final var expectedTask = TestDomainDataGenerator.anyTask(1L);

        final var sut = createSUT();

        given(
            sut.createBacklogIfDoesntExist.execute(
                UUID.randomUUID(),
                backlogStartDate,
                backlogDuration,
                Optional.of(sut.transaction)
            )
        ).willReturn(backlogId);

        given(
            sut.tasksRepository.createTask(
                backlogId,
                newTaskData,
                Optional.of(sut.transaction)
            )
        ).willReturn(expectedTask);

        // When
        final var createdTask = sut.useCase.execute(
            backlogStartDate,
            backlogDuration,
            newTaskData
        );

        // Then
        then(sut.transactionFactory).should(times(1))
            .make();

        then(sut.transaction).should(times(1))
            .begin();

        then(sut.createBacklogIfDoesntExist).should(times(1))
            .execute(
                ownerId,
                backlogStartDate,
                backlogDuration,
                Optional.of(sut.transaction)
            );

        then(sut.tasksRepository).should(times(1))
            .createTask(
                backlogId,
                newTaskData,
                Optional.of(sut.transaction)
            );

        then(sut.transaction).should(times(1))
            .commit();

        then(sut.transaction).should(never())
            .rollback();

        assertThat(createdTask).isNotNull();
        assertThat(createdTask).isEqualTo(expectedTask);
    }

    @Test
    void execute_givenFail_thenShouldRollbackTransaction() throws Exception {

        // Given
        final var ownerId = anyUserId();
        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();

        final var backlogId = 1L;

        final var newTaskData = new NewTaskData(
            "title",
            Task.Priority.HIGH,
            "description"
        );

        final var sut = createSUT();

        given(
            sut.createBacklogIfDoesntExist.execute(
                ownerId,
                backlogStartDate,
                backlogDuration,
                Optional.of(sut.transaction)
            )
        ).willReturn(backlogId);

        given(
            sut.tasksRepository.createTask(
                eq(backlogId),
                any(NewTaskData.class),
                any()
            )
        ).willThrow(new RuntimeException());

        // When
        final var exception = assertThrows(
            RuntimeException.class,
            () -> sut.useCase.execute(
                backlogStartDate,
                backlogDuration,
                newTaskData
            )
        );

        // Then
        assertThat(exception).isNotNull();

        then(sut.transactionFactory).should(times(1))
            .make();

        then(sut.transaction).should(times(1))
            .begin();

        then(sut.transaction).should(never())
            .commit();

        then(sut.transaction).should(times(1))
            .rollback();
    }
}
