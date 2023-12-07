package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.azatdev.dailytasks.data.repositories.TestEntityDataGenerator;
import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;

@DataJpaTest
class JPATasksRepositoryTests {

    // Fields

    @Autowired
    TestEntityDataGenerator testData;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private JPATasksRepository jpaTasksRepository;

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
            tasks[i] = entityManager.persistAndFlush(
                testData.anyTaskData(
                    owner,
                    backlog,
                    i
                )
            );
        }

        return tasks;
    }

    @Test
    void findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc_givenExistingTasks_thenReturnCorrectTasks() {

        // Given
        final var owner = testData.givenExistingUser("rightOwner");
        final var wrongOwner = testData.givenExistingUser("wrongOwner");

        final var backlog = testData.givenExistingWeekBacklog(owner);
        final var wrongBacklog = testData.givenExistingWeekBacklog(owner);

        final var tasksBacklog1 = givenExistingTasks(
            owner,
            backlog,
            3
        );
        final var tasksBacklog2 = givenExistingTasks(
            owner,
            wrongBacklog,
            3
        );

        final var tasksBacklog3 = givenExistingTasks(
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
        final var owner = testData.givenExistingUser();
        final var backlog = testData.givenExistingWeekBacklog(owner);
        final int lastOrder = 10;

        entityManager.persistAndFlush(
            testData.anyTaskData(
                owner,
                backlog,
                lastOrder
            )
        );

        // When
        final var result = jpaTasksRepository.findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc(
            owner.getId(),
            backlog.getId()
        );

        // Then
        assertThat(result).isNotEmpty();
    }
}
