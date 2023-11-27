package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomain;
import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomainImpl;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

interface JPATasksRepository extends JpaRepository<TaskData, Long> {

    Iterable<TaskData> findAllByBacklogIdOrderByOrderInBacklogAsc(Long backlogId);
}

@Repository
class TasksRepositoryImpl implements TasksRepositoryList {

    // Fields

    private final JPATasksRepository jpaTasksRepository;
    private final MapTaskDataToDomain mapTaskDataToDomain;

    // Constructors

    public TasksRepositoryImpl(
        JPATasksRepository jpaTasksRepository,
        MapTaskDataToDomain mapTaskDataToDomain
    ) {
        this.jpaTasksRepository = jpaTasksRepository;
        this.mapTaskDataToDomain = mapTaskDataToDomain;
    }

    // Methods

    @Override
    public Result<List<Task>, Error> list(Long backlogId) {

        final Iterable<TaskData> items = jpaTasksRepository.findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId);

        final List<Task> tasks = new ArrayList<>();

        for (TaskData item : items) {
            tasks.add(mapTaskDataToDomain.map(item));
        }

        return Result.success(tasks);
    }
}

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
