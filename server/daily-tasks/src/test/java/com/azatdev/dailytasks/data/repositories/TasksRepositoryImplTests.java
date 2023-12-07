package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomain;
import com.azatdev.dailytasks.data.repositories.data.TasksRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaGetBacklogReference;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaGetUserReference;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;

@ExtendWith(MockitoExtension.class)
class TasksRepositoryImplTests {

    @Autowired
    TestDataManager dm;

    @BeforeEach
    void setupTestDataManager() {
        dm = new TestDataManager();
    }

    private record SUT(
        TasksRepositoryImpl tasksRepository,
        JpaGetUserReference jpaGetUserReference,
        JpaGetBacklogReference jpaGetBacklogReference,
        JpaTasksRepository jpaTasksRepository,
        MapTaskDataToDomain mapTaskDataToDomain
    ) {
    };

    private SUT createSUT() {
        final var japGetUserReference = mock(JpaGetUserReference.class);
        final var jpaGetBacklogReference = mock(JpaGetBacklogReference.class);
        final var jpaTasksRepository = mock(JpaTasksRepository.class);
        final var mapTaskDataToDomain = mock(MapTaskDataToDomain.class);

        given(mapTaskDataToDomain.execute(any())).willReturn(null);

        return new SUT(
            new TasksRepositoryImpl(
                japGetUserReference,
                jpaGetBacklogReference,
                jpaTasksRepository,
                mapTaskDataToDomain
            ),
            japGetUserReference,
            jpaGetBacklogReference,
            jpaTasksRepository,
            mapTaskDataToDomain
        );
    }

    @Test
    void list_givenExistingTasks_thenReturnTasksFromRepository() {

        // Given
        final var user = dm.givenUserDataWithId();
        final var userId = user.getId();

        final var backlog = dm.givenBacklogDataWithId();
        final var backlogId = backlog.getId();

        final var existingTaskData = dm.givenTaskDataWithId();

        final List<TaskData> existingTasksData = List.of(existingTaskData);

        final var sut = createSUT();

        given(
            sut.jpaTasksRepository.findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc(
                userId,
                backlogId
            )
        ).willReturn(existingTasksData);

        // When
        final var receivedTasks = sut.tasksRepository.list(
            userId,
            backlogId
        );

        // Then
        final var expectedTaskIds = existingTasksData.stream()
            .map(TaskData::getId)
            .toList();

        then(sut.jpaTasksRepository).should(times(1))
            .findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc(
                userId,
                backlogId
            );

        assertThat(receivedTasks).extractingResultOf("id")
            .isEqualTo(expectedTaskIds);

        then(sut.jpaTasksRepository).should(times(1))
            .findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc(
                userId,
                backlogId
            );
    }

    @Test
    void createTask_givenEmptyDb_thenCreatATask() {

        // Given
        final var ownerReference = dm.givenUserDataWithId();
        final var ownerId = ownerReference.getId();

        final var backlogReference = dm.givenBacklogDataWithId();
        final var backlogId = backlogReference.getId();

        final int lastOrderInBacklog = 111;
        final int expectedOrderInBacklog = lastOrderInBacklog + 1;
        final var newTaskData = new NewTaskData(
            "title",
            Task.Priority.LOW,
            "description"
        );

        final var savedTaskData = new TaskData(
            ownerReference,
            backlogReference,
            expectedOrderInBacklog,
            newTaskData.title(),
            newTaskData.description(),
            TaskData.Status.NOT_STARTED,
            TaskData.Priority.LOW
        );

        final var sut = createSUT();

        given(sut.jpaTasksRepository.saveAndFlush(any(TaskData.class))).willReturn(savedTaskData);

        final var orderInBacklogProjection = mock(JpaTasksRepository.OrderInBacklogProjection.class);
        given(orderInBacklogProjection.getOrderInBacklog()).willReturn(lastOrderInBacklog);

        given(
            sut.jpaTasksRepository.findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc(
                ownerId,
                backlogId
            )
        ).willReturn(Optional.of(orderInBacklogProjection));

        final var mappedTask = mock(Task.class);
        given(sut.mapTaskDataToDomain.execute(any(TaskData.class))).willReturn(mappedTask);

        // When
        final var createdTask = sut.tasksRepository.createTask(
            ownerId,
            backlogId,
            newTaskData,
            Optional.empty()
        );

        then(sut.jpaTasksRepository).should(times(1))
            .findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc(
                ownerId,
                backlogId
            );

        then(sut.jpaTasksRepository).should(times(1))
            .saveAndFlush(any());

        assertThat(createdTask).isNotNull();
        assertThat(createdTask).isEqualTo(mappedTask);
    }
}
