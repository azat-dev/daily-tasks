package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

import jakarta.annotation.Nonnull;

interface AddTaskInBacklogUseCase {

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

interface TasksRepositoryAdd {

    enum Error {
        INTERNAL_ERROR,
        BACKLOG_NOT_FOUND
    }

    Result<Task, Error> add(
        Long backlogId,
        NewTaskData newTaskData
    );
}

interface AddBacklogForDateIfDoesntExistUseCase {

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

class AddTaskInBacklogUseCaseImpl implements AddTaskInBacklogUseCase {

    private final AddBacklogForDateIfDoesntExistUseCase addBacklogUseCase;
    private final TasksRepositoryAdd tasksRepository;

    public AddTaskInBacklogUseCaseImpl(
        AddBacklogForDateIfDoesntExistUseCase addBacklogUseCase,
        TasksRepositoryAdd tasksRepository
    ) {
        this.addBacklogUseCase = addBacklogUseCase;
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Result<Task, Error> execute(
        LocalDate date,
        Backlog.Duration backlogDuration
    ) {

        final var backlogIdResult = addBacklogUseCase.execute(
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

class AddTaskInBacklogUseCaseImplTests {

    private record SUT(
        AddTaskInBacklogUseCase useCase,
        AddBacklogForDateIfDoesntExistUseCase addBacklogUseCase,
        TasksRepositoryAdd tasksRepository
    ) {
    }

    private SUT createSUT() {
        final var addBacklogUseCase = mock(AddBacklogForDateIfDoesntExistUseCase.class);

        final var tasksRepository = mock(TasksRepositoryAdd.class);

        return new SUT(
            new AddTaskInBacklogUseCaseImpl(
                addBacklogUseCase,
                tasksRepository
            ),
            addBacklogUseCase,
            tasksRepository
        );
    }

    @Test
    void executeShouldAddBacklogAddTaskAndReturnCreatedTaskTest() {

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
            sut.addBacklogUseCase.execute(
                backlogStartDate,
                backlogDuration
            )
        ).willReturn(Result.success(backlogId));

        given(
            sut.tasksRepository.add(
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
        then(sut.addBacklogUseCase).should(times(1))
            .execute(
                backlogStartDate,
                backlogDuration
            );

        then(sut.tasksRepository).should(times(1))
            .add(
                backlogId,
                newTaskData
            );

        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(expectedTask);
    }
}
