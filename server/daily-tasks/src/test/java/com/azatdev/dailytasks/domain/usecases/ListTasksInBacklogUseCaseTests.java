package com.azatdev.dailytasks.domain.usecases;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;
import com.azatdev.dailytasks.utils.Result;

public class ListTasksInBacklogUseCaseTests {
    
    private record SUT(
        ListTasksInBacklogUseCase listTasksInBacklogUseCase,
        BacklogRepositoryGet backlogRepository,
        TasksRepositoryList tasksRepository,
        AdjustDateToStartOfBacklog adjustBacklogTime
    ) {

    }

    private SUT createSUT() {
        BacklogRepositoryGet backlogRepository = mock(BacklogRepositoryGet.class);
        TasksRepositoryList tasksRepository = mock(TasksRepositoryList.class);
        AdjustDateToStartOfBacklog adjustBacklogTime = mock(AdjustDateToStartOfBacklog.class);

        var useCase = new ListTasksInBacklogUseCaseImpl(
            backlogRepository,
            tasksRepository,
            adjustBacklogTime
        );

        return new SUT(
            useCase,
            backlogRepository,
            tasksRepository,
            adjustBacklogTime
        );
    }

    @Test
    void executeEmptyDbShouldReturnEmptyListTest() {
        
        // Given
        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();
        final Task[] expectedTasks = {};
        final LocalDate adjustedBacklogStartDate = backlogStartDate;

        final var sut = createSUT();

        given(sut.adjustBacklogTime.calculate(backlogStartDate, backlogDuration))
            .willReturn(adjustedBacklogStartDate);

        given(sut.backlogRepository.getBacklogId(backlogStartDate, backlogDuration))
            .willReturn(Result.success(Optional.empty()));
    

        // When
        var result = sut.listTasksInBacklogUseCase.execute(backlogStartDate, backlogDuration);

        // Then
        then(sut.backlogRepository)
            .should(times(1))
            .getBacklogId(adjustedBacklogStartDate, backlogDuration);
        
        then(sut.tasksRepository)
            .should(never())
            .list(any());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(expectedTasks);
    }


    @Test
    void executeEmptyBacklogShouldReturnEmptyListTest() {
        
        // Given
        UUID backlogId = UUID.randomUUID();

        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();
        final Task[] expectedTasks = {};
        final LocalDate adjustedBacklogStartDate = backlogStartDate;

        final var sut = createSUT();

        given(sut.adjustBacklogTime.calculate(backlogStartDate, backlogDuration))
            .willReturn(adjustedBacklogStartDate);

        given(sut.backlogRepository.getBacklogId(backlogStartDate, backlogDuration))
            .willReturn(Result.success(Optional.of(backlogId)));
    
        given(sut.tasksRepository.list(backlogId))
            .willReturn(Result.success(expectedTasks));

        // When
        var result = sut.listTasksInBacklogUseCase.execute(backlogStartDate, backlogDuration);

        // Then
        then(sut.backlogRepository)
            .should(times(1))
            .getBacklogId(adjustedBacklogStartDate, backlogDuration);
        
        then(sut.tasksRepository)
            .should(times(1))
            .list(backlogId);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(expectedTasks);
    }
}