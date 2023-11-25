package com.azatdev.dailytasks.data.repositories;

import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;

@Entity
@Table(name = "tasks")
class TaskData {

    // Types

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    // Fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "backlog_id", nullable = false)
    private Long backlogId;

    @Column(name = "order_in_backlog")
    private Integer orderInBacklog;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private Priority priority;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status status;

    // Constructors

    public TaskData(
        Long backlogId,
        Integer orderInBacklog,
        String title,
        String description,
        Status status,
        Priority priority
    ) {
        this.backlogId = backlogId;
        this.orderInBacklog = orderInBacklog;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.description = description;
    }


    @PrePersist
    void prePersist() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}

interface JPATasksRepository extends JpaRepository<TaskData, Long> {

    Iterable<TaskData> findAllByBacklogIdOrderByOrderInBacklogAsc(Long backlogId);
}

class TasksRepositoryImpl implements TasksRepositoryList {

    // Fields

    private final JPATasksRepository jpaTasksRepository;

    // Constructors

    public TasksRepositoryImpl(JPATasksRepository jpaTasksRepository) {
        this.jpaTasksRepository = jpaTasksRepository;
    }

    // Methods

    @Override
    public Result<Task[], Error> list(Long backlogId) {
        return Result.success(new Task[]{});
    }
}

@ExtendWith(MockitoExtension.class)
public class TasksRepositoryTests {

    // Fields

    @Autowired
    TestEntityManager entityManager;

    @Mock
    JPATasksRepository jpaTasksRepository;

    @InjectMocks
    TasksRepositoryImpl tasksRepository;
    
    // Helpers

    private Long anyBacklogId() {
        return 111L;
    }

    // Tests

    @Test
    void listTasksReturnsShouldReturnTasksFromRepositoryTest() {
        
        // Given
        final var backlogId = anyBacklogId();
        final String[] expectedTaskTitles = { "1", "2", "3" };

        given(jpaTasksRepository.findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId))
            .willReturn(null);

        // When
        var result = tasksRepository.list(backlogId);

        // Then
        assertThat(result.isSuccess()).isTrue();

        var receivedTasks = result.getValue();
        var receivedTaskTitles = Arrays.stream(receivedTasks)
            .map(task -> task.title()).toArray(String[]::new);

        assertThat(receivedTaskTitles).isEqualTo(expectedTaskTitles);

        then(jpaTasksRepository)
            .should(times(1))
            .findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId);

        assertThat(1).isEqualTo(5);
    }
}
