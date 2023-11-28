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
            backlogDuration,
            newTaskData
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
