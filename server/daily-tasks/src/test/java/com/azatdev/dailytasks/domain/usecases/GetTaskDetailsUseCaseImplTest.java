package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomain;
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

    private final MapTaskDataToDomain mapper;
    private final JpaTasksRepository tasksRepository;

    public GetTaskDetailsUseCaseImpl(
        MapTaskDataToDomain mapper,
        JpaTasksRepository tasksRepository
    ) {
        this.mapper = mapper;
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Optional<Task> execute(
        UUID userId,
        long taskId
    ) {
        return null;
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

        given(sut.tasksRepository.findById(any())).willReturn(Optional.empty());

        // When
        final var result = sut.useCase.execute(
            userId,
            taskId
        );

        // Then
        assertThat(result).isEmpty();
    }
}
