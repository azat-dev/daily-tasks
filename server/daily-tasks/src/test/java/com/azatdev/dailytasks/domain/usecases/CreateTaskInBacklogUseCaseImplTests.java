package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

import jakarta.annotation.Nonnull;

interface CreateTaskInBacklogUseCase {

    enum Error {
        INTERNAL_ERROR
    }

    Result<Task, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration
    );
}

record NewTaskData(
    @Nonnull String title,
    Task.Priority priority,
    @Nonnull String description
) {
}

interface TasksRepositoryCreate {

    enum Error {
        INTERNAL_ERROR,
        BACKLOG_NOT_FOUND
    }

    Result<Task, Error> createTask(
        Long backlogId,
        NewTaskData newTaskData
    );
}

interface CreateBacklogForDateIfDoesntExistUseCase {

    enum Error {
        INTERNAL_ERROR
    }

    /**
     * Adds backlog for given date and duration if it does not exist.
     * 
     * @param date
     * @param backlogDuration
     * @return Backlog Id
     */
    Result<Long, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration
    );
}

class CreateTaskInBacklogUseCaseImpl implements CreateTaskInBacklogUseCase {

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
        Backlog.Duration backlogDuration
    ) {

        final var backlogIdResult = createBacklogIfDoesntExistUseCase.execute(
            date,
            backlogDuration
        );

        if (!backlogIdResult.isSuccess()) {
            return Result.failure(Error.INTERNAL_ERROR);
        }

        final var backlogId = backlogIdResult.getValue();

        return null;
    }
}

class CreateTaskInBacklogUseCaseImplTests {

    private record SUT(
        CreateTaskInBacklogUseCase useCase,
        CreateBacklogForDateIfDoesntExistUseCase createBacklogIfDoesntExist,
        TasksRepositoryCreate tasksRepository
    ) {
    }

    private SUT createSUT() {
        final var createBacklogIfDoesntExistUseCase = mock(CreateBacklogForDateIfDoesntExistUseCase.class);

        final var tasksRepository = mock(TasksRepositoryCreate.class);

        return new SUT(
            new CreateTaskInBacklogUseCaseImpl(
                createBacklogIfDoesntExistUseCase,
                tasksRepository
            ),
            createBacklogIfDoesntExistUseCase,
            tasksRepository
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
                backlogDuration
            )
        ).willReturn(Result.success(backlogId));

        given(
            sut.tasksRepository.createTask(
                backlogId,
                newTaskData
            )
        ).willReturn(Result.success(expectedTask));

        // When
        final var result = sut.useCase.execute(
            backlogStartDate,
            backlogDuration
        );

        // Then
        then(sut.createBacklogIfDoesntExist).should(times(1))
            .execute(
                backlogStartDate,
                backlogDuration
            );

        then(sut.tasksRepository).should(times(1))
            .createTask(
                backlogId,
                newTaskData
            );

        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(expectedTask);
    }
}
