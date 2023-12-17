package com.azatdev.dailytasks.data.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.data.repositories.data.GetTaskDaoImpl;
import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomain;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.GetTaskDao;
import com.azatdev.dailytasks.domain.models.Task;

class GetTaskDaoTest {

    private record SUT(
        GetTaskDao dao,
        MapTaskDataToDomain mapper,
        JpaTasksRepository tasksRepository
    ) {
    }

    private SUT createSUT() {
        final var mapper = mock(MapTaskDataToDomain.class);
        final var tasksRepository = mock(JpaTasksRepository.class);

        final var dao = new GetTaskDaoImpl(
            mapper,
            tasksRepository
        );

        return new SUT(
            dao,
            mapper,
            tasksRepository
        );
    }

    private long anyTaskId() {
        return 111L;
    }

    @Test
    void execute_givenEmptyDb_thenReturnEmptyOptional() {

        // Given
        final var taskId = anyTaskId();
        final var sut = createSUT();

        given(sut.tasksRepository.findById(any())).willReturn(Optional.empty());

        // When
        final var result = sut.dao.execute(taskId);

        // Then
        then(sut.tasksRepository).should(times(1))
            .findById(taskId);
        assertThat(result).isEmpty();
    }

    @Test
    void execute_givenExistingTask_thenReturnMappedData() {

        // Given
        final var taskId = anyTaskId();
        final var sut = createSUT();

        final var existingTaskData = mock(TaskData.class);
        final var mappedTask = mock(Task.class);

        given(sut.tasksRepository.findById(any())).willReturn(Optional.of(existingTaskData));
        given(sut.mapper.execute(any())).willReturn(mappedTask);

        // When
        final var result = sut.dao.execute(taskId);

        // Then
        then(sut.tasksRepository).should(times(1))
            .findById(taskId);

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isSameAs(mappedTask);
    }
}
