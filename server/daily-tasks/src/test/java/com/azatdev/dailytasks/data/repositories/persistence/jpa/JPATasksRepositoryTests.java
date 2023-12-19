package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.azatdev.dailytasks.data.dao.data.user.UserData;
import com.azatdev.dailytasks.data.dao.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.dao.persistence.entities.TaskData;
import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaTasksRepository;
import com.azatdev.dailytasks.data.repositories.TestEntityDataGenerator;

@DataJpaTest
@Import(TestEntityDataGenerator.class)
class JpaTasksRepositoryTests {

    // Fields

    @Autowired
    TestEntityDataGenerator dm;

    @Autowired
    private JpaTasksRepository jpaTasksRepository;

    // Methods

    @Test
    void findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc_givenEmptyDb_thenReturnEmptyList() {

        // Given
        final var ownerId = UUID.randomUUID();
        final var backlogId = 1L;

        // Empty DB

        // When
        var result = jpaTasksRepository.findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc(
            ownerId,
            backlogId
        );

        // Then
        assertThat(result).isEmpty();
    }

    private TaskData[] givenExistingTasks(
        UserData owner,
        BacklogData backlog,
        int count
    ) {
        final var tasks = new TaskData[count];

        for (var i = 0; i < count; i++) {
            tasks[i] = dm.givenExistingTaskData(
                owner,
                backlog,
                i
            );
        }

        return tasks;
    }

    @Test
    void findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc_givenExistingTasks_thenReturnCorrectTasks() {

        // Given
        final var owner = dm.givenExistingUser("rightOwner");
        final var wrongOwner = dm.givenExistingUser("wrongOwner");

        final var backlog = dm.givenExistingDayBacklog(owner);
        final var wrongBacklog = dm.givenExistingDayBacklog(
            owner,
            backlog.getStartDate()
                .plusDays(1)
        );

        final var tasksBacklog1 = givenExistingTasks(
            owner,
            backlog,
            3
        );

        givenExistingTasks(
            owner,
            wrongBacklog,
            3
        );

        givenExistingTasks(
            wrongOwner,
            backlog,
            3
        );

        // When
        var foundTasks = jpaTasksRepository.findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc(
            owner.getId(),
            backlog.getId()
        );

        // Then
        assertThat(foundTasks).containsExactly(tasksBacklog1);
    }

    @Test
    void findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc_givenEmptyDb_ThenReturnNull() {

        // Given
        final var ownerId = UUID.randomUUID();
        final var backlogId = 1L;

        // Empty DB

        // When
        var result = jpaTasksRepository.findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc(
            ownerId,
            backlogId
        );

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc_givenExistingTasks_thenReturnLastOrder() {

        // Given
        final var owner = dm.givenExistingUser("owner");
        final var wrongOwner = dm.givenExistingUser("wrongOwner");

        final var backlog = dm.givenExistingWeekBacklog(owner);
        final var wrongBacklog = dm.givenExistingDayBacklog(owner);

        final int numberOfTasks = 5;

        givenExistingTasks(
            owner,
            backlog,
            numberOfTasks
        );
        givenExistingTasks(
            owner,
            wrongBacklog,
            numberOfTasks + 1
        );
        givenExistingTasks(
            wrongOwner,
            backlog,
            numberOfTasks + 2
        );

        // When
        final var result = jpaTasksRepository.findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc(
            owner.getId(),
            backlog.getId()
        );

        // Then
        assertThat(result).isNotEmpty();
        assertThat(
            result.get()
                .getOrderInBacklog()
        ).isEqualTo(numberOfTasks - 1);
    }

    @Test
    void findByOwnerIdAndId_whenTaskExist_thenMustReturnCorrectTask() {

        // Given
        final var owner = dm.givenExistingUser("owner");
        final var wrongOwner = dm.givenExistingUser("wrongOwner");

        final var backlog = dm.givenExistingWeekBacklog(owner);

        final var correctTask = dm.givenExistingTaskData(
            owner,
            backlog,
            0
        );
        final var wrongOwnerTask = dm.givenExistingTaskData(
            wrongOwner,
            backlog,
            0
        );
        final var wrongIdTask = dm.givenExistingTaskData(
            owner,
            backlog,
            0
        );

        // When
        final var result = jpaTasksRepository.findByOwnerIdAndId(
            owner.getId(),
            correctTask.getId()
        );

        // Then
        assertThat(result.get()).isEqualTo(correctTask);
        assertThat(result.get()).isNotIn(
            wrongIdTask,
            wrongOwnerTask
        );
    }
}
