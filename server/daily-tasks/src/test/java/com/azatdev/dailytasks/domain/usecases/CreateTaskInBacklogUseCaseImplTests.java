package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryCreate;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

@FunctionalInterface
interface TransactionFactory {
    Transaction make();
}

interface Transaction {
    void begin();
    void commit();
}

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

        final var createBacklogIfDoesntExistUseCase = mock(CreateBacklogForDateIfDoesntExistUseCase.class);

        final var tasksRepository = mock(TasksRepositoryCreate.class);

        return new SUT(
            new CreateTaskInBacklogUseCaseImpl(
                createBacklogIfDoesntExistUseCase,
                tasksRepository
            ),
            createBacklogIfDoesntExistUseCase,
            tasksRepository,
            transactionFactory,
            transaction
        );
    }

    @Test
    void executeShouldCreateBacklogCreateTaskAndReturnCreatedTaskTest() {

        // Given
        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();

        final var backlogId = 1L;

        final var newTaskData = new NewTaskData(
            "title",
            Task.Priority.HIGH,
            "description"
        );

        final var expectedTask = TestDomainDataGenerator.anyTask(1L);

        final var sut = createSUT();

        given(
            sut.createBacklogIfDoesntExist.execute(
                backlogStartDate,
                backlogDuration,
                sut.transaction
            )
        ).willReturn(Result.success(backlogId));

        given(
            sut.tasksRepository.createTask(
                backlogId,
                newTaskData,
                sut.transaction
            )
        ).willReturn(Result.success(expectedTask));

        // When
        final var result = sut.useCase.execute(
            backlogStartDate,
            backlogDuration,
            newTaskData
        );

        // Then
        then(sut.transactionFactory).should(times(1))
            .createTransaction();

        then(sut.transaction).should(times(1))
            .begin();

            then(sut.createBacklogIfDoesntExist).should(times(1))
            .execute(
                backlogStartDate,
                backlogDuration,
                sut.transaction
            );

        then(sut.tasksRepository).should(times(1))
            .createTask(
                backlogId,
                newTaskData,
                sut.transaction
            );

        then(sut.transaction).should(times(1))
            .commit();

        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(expectedTask);
    }
}
