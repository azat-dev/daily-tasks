package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.azatdev.dailytasks.data.repositories.TestEntityDataGenerator;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;

@DataJpaTest
class JPATasksRepositoryTests {

    // Fields

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private JPATasksRepository jpaTasksRepository;

    // Methods

    @Test
    void findAllByBacklogInEmptyDbShouldReturnEmptyListTest() {

        // Given
        final var backlogId = 1L;

        // Empty DB

        // When
        var result = jpaTasksRepository.findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId);

        // Then
        assertThat(result).isEmpty();
    }

    private TaskData[] generateExistingTasks(
        Long backlogId,
        int count
    ) {
        final var tasks = new TaskData[count];

        for (var i = 0; i < count; i++) {
            tasks[i] = entityManager.persistAndFlush(
                TestEntityDataGenerator.anyTaskData(
                    backlogId,
                    i
                )
            );
        }

        return tasks;
    }

    @Test
    void findAllByBacklogShouldReturnCorrectItemsTest() {

        // Given
        final var backlogId1 = 1L;
        final var backlogId2 = 2L;

        final var tasksBacklog1 = generateExistingTasks(
            backlogId1,
            3
        );
        final var tasksBacklog2 = generateExistingTasks(
            backlogId2,
            3
        );

        // When
        var foundTasks = jpaTasksRepository.findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId1);

        // Then
        assertThat(foundTasks).containsExactly(tasksBacklog1);
    }
}
