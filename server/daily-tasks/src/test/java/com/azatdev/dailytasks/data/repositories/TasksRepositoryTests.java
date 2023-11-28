package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomainImpl;
import com.azatdev.dailytasks.data.repositories.data.TasksRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPATasksRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;

@ExtendWith(MockitoExtension.class)
public class TasksRepositoryTests {

    // Fields

    private record SUT(
        TasksRepositoryList tasksRepository,
        JPATasksRepository jpaTasksRepository
    ) {

    };

    // Helpers

    private SUT createSUT() {
        final var jpaTasksRepository = mock(JPATasksRepository.class);

        return new SUT(
            new TasksRepositoryImpl(
                jpaTasksRepository,
                new MapTaskDataToDomainImpl()
            ),
            jpaTasksRepository
        );
    }

    // Helpers

    private Long anyBacklogId() {
        return 111L;
    }

    // Tests

    @Test
    void listTasksReturnsShouldReturnTasksFromRepositoryTest() {

        // Given
        final var backlogId = anyBacklogId();

        final List<TaskData> existingTasksData = List.of(
            TestEntityDataGenerator.anyTaskData(
                backlogId,
                1
            )
        );

        final var sut = createSUT();

        given(sut.jpaTasksRepository.findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId))
            .willReturn(existingTasksData);

        // When
        final var result = sut.tasksRepository.list(backlogId);

        // Then
        assertThat(result.isSuccess()).isTrue();

        final var receivedTasks = result.getValue();

        final var expectedTaskTitles = existingTasksData.stream()
            .map(TaskData::getTitle)
            .toList();

        assertThat(receivedTasks).extracting("title")
            .isEqualTo(expectedTaskTitles);

        then(sut.jpaTasksRepository).should(times(1))
            .findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId);
    }
}
