package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomain;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.models.Task;

@FunctionalInterface
interface GetTaskDetailsUseCase {

    Optional<Task> execute(
        UUID userId,
        long taskId
    );
}

final class GetTaskDetailsUseCaseImpl implements GetTaskDetailsUseCase {

    private final MapTaskDataToDomain mapTaskDataToDomain;
    private final JpaTasksRepository tasksRepository;

    public GetTaskDetailsUseCaseImpl(
        MapTaskDataToDomain mapper,
        JpaTasksRepository tasksRepository
    ) {
        this.mapTaskDataToDomain = mapper;
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Optional<Task> execute(
        UUID userId,
        long taskId
    ) {
        return tasksRepository.findByOwnerIdAndId(
            userId,
            taskId
        )
            .map(mapTaskDataToDomain::execute);
    }
}

class GetTaskDetailsUseCaseImplTest {

    private record SUT(
        GetTaskDetailsUseCase useCase,
        MapTaskDataToDomain mapper,
        JpaTasksRepository tasksRepository
    ) {
    }

    private SUT createSUT() {
        final var mapper = mock(MapTaskDataToDomain.class);
        final var tasksRepository = mock(JpaTasksRepository.class);

        final var useCase = new GetTaskDetailsUseCaseImpl(
            mapper,
            tasksRepository
        );

        return new SUT(
            useCase,
            mapper,
            tasksRepository
        );
    }

    private long anyTaskId() {
        return 111L;
    }

    private UUID anyUserId() {
        return UUID.randomUUID();
    }

    @Test
    void execute_givenEmptyDb_thenReturnEmptyOptional() {

        // Given
        final var userId = anyUserId();
        final var taskId = anyTaskId();
        final var sut = createSUT();

        given(
            sut.tasksRepository.findByOwnerIdAndId(
                any(),
                any()
            )
        ).willReturn(Optional.empty());

        // When
        final var result = sut.useCase.execute(
            userId,
            taskId
        );

        // Then
        then(sut.tasksRepository).should(times(1))
            .findByOwnerIdAndId(
                userId,
                taskId
            );
        assertThat(result).isEmpty();
    }

    @Test
    void execute_givenExistingTask_thenReturnMappedData() {

        // Given
        final var userId = anyUserId();
        final var taskId = anyTaskId();
        final var sut = createSUT();

        final var existingTaskData = mock(TaskData.class);
        final var mappedTask = mock(Task.class);

        given(
            sut.tasksRepository.findByOwnerIdAndId(
                any(),
                any()
            )
        ).willReturn(Optional.of(existingTaskData));
        given(sut.mapper.execute(any())).willReturn(mappedTask);

        // When
        final var result = sut.useCase.execute(
            userId,
            taskId
        );

        // Then
        then(sut.tasksRepository).should(times(1))
            .findByOwnerIdAndId(
                userId,
                taskId
            );

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isSameAs(mappedTask);
    }
}
