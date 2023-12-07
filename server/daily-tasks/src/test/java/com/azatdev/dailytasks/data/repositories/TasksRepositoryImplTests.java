package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.azatdev.dailytasks.data.repositories.data.MapTaskDataToDomainImpl;
import com.azatdev.dailytasks.data.repositories.data.TasksRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPATasksRepository;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryCreate;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;

interface TasksRepository extends TasksRepositoryCreate, TasksRepositoryList {
}

@ExtendWith(MockitoExtension.class)
class TasksRepositoryImplTests {

    @Autowired
    private TestDataManager dm;

    private record SUT(
        TasksRepositoryImpl tasksRepository,
        JPATasksRepository jpaTasksRepository
    ) {
    };

    private SUT createSUT() {
        final var jpaUsersRepository = mock(JpaUsersRepository.class);
        final var jpaTasksRepository = mock(JPATasksRepository.class);

        return new SUT(
            new TasksRepositoryImpl(
                jpaUsersRepository,
                jpaTasksRepository,
                new MapTaskDataToDomainImpl()
            ),
            jpaTasksRepository
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
        final var expectedTaskTitles = existingTasksData.stream()
            .map(TaskData::getTitle)
            .toList();

        assertThat(receivedTasks).extracting("title")
            .isEqualTo(expectedTaskTitles);

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

        final var orderInBacklogProjection = mock(JPATasksRepository.OrderInBacklogProjection.class);
        given(orderInBacklogProjection.getOrderInBacklog()).willReturn(lastOrderInBacklog);

        given(
            sut.jpaTasksRepository.findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc(
                ownerId,
                backlogId
            )
        ).willReturn(Optional.of(orderInBacklogProjection));

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
            .saveAndFlush(eq(savedTaskData));

        assertThat(createdTask).isNotNull();
        assertThat(createdTask).isEqualTo(savedTaskData.getId());
    }
}
