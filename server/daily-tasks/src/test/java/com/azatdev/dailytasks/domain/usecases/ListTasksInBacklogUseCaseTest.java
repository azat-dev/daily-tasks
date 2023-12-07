package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepositoryGet;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.usecases.utils.AdjustDateToStartOfBacklog;

public class ListTasksInBacklogUseCaseTest {

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

    private UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenNotExistingBacklog_thenReturnEmptyList() {

        // Given
        final var ownerId = anyUserId();
        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();
        final var adjustedBacklogStartDate = backlogStartDate;

        final var sut = createSUT();

        given(
            sut.adjustBacklogTime.calculate(
                backlogStartDate,
                backlogDuration
            )
        ).willReturn(adjustedBacklogStartDate);

        given(
            sut.backlogRepository.getBacklogId(
                ownerId,
                backlogStartDate,
                backlogDuration
            )
        ).willReturn(Optional.empty());

        // When
        var foundTasks = sut.listTasksInBacklogUseCase.execute(
            ownerId,
            backlogStartDate,
            backlogDuration
        );

        // Then
        then(sut.backlogRepository).should(times(1))
            .getBacklogId(
                ownerId,
                adjustedBacklogStartDate,
                backlogDuration
            );

        then(sut.tasksRepository).should(never())
            .list(
                any(UUID.class),
                any(long.class)
            );

        assertThat(foundTasks).isNotNull();
        assertThat(foundTasks).isEmpty();
    }

    @Test
    void execute_givenExistingBacklog_thenReturnTasksInBacklog() {

        // Given
        final var ownerId = anyUserId();
        final var backlogId = anyBacklogId();

        final var backlogDuration = Backlog.Duration.DAY;
        final var backlogStartDate = LocalDate.now();
        final var expectedTasks = List.of(
            TestDomainDataGenerator.anyTask(1L),
            TestDomainDataGenerator.anyTask(1L)
        );

        final LocalDate adjustedBacklogStartDate = backlogStartDate;

        final var sut = createSUT();

        given(
            sut.adjustBacklogTime.calculate(
                backlogStartDate,
                backlogDuration
            )
        ).willReturn(adjustedBacklogStartDate);

        given(
            sut.backlogRepository.getBacklogId(
                ownerId,
                backlogStartDate,
                backlogDuration
            )
        ).willReturn(Optional.of(backlogId));

        given(
            sut.tasksRepository.list(
                ownerId,
                backlogId
            )
        ).willReturn(expectedTasks);

        // When
        final var foundTasks = sut.listTasksInBacklogUseCase.execute(
            ownerId,
            backlogStartDate,
            backlogDuration
        );

        // Then
        then(sut.backlogRepository).should(times(1))
            .getBacklogId(
                ownerId,
                adjustedBacklogStartDate,
                backlogDuration
            );

        then(sut.tasksRepository).should(times(1))
            .list(
                ownerId,
                backlogId
            );

        assertThat(foundTasks).isNotNull();
        assertThat(foundTasks).isEqualTo(expectedTasks);
    }

    private Long anyBacklogId() {
        return 111L;
    }
}
