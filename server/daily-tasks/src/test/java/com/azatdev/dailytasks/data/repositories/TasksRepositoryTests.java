package com.azatdev.dailytasks.data.repositories;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import javax.swing.text.html.Option;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.BDDMockito.*;

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

    // Fields

    @Id
    private Long id;

    @Column(name = "backlog_id")
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

    TaskData findByBacklogIdOrderByOrderInBacklogAsc(Long backlogId);
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

    @Mock
    JPATasksRepository jpaTasksRepository;


    @InjectMocks
    TasksRepositoryImpl tasksRepository;
    

    private Long anyBacklogId() {
        return 111L;
    }

    void listEmptyDbShouldReturnEmptyListTest() {
        
        // Given
        final var backlogId = anyBacklogId();
        final Task[] expectedTasks = {};

        given(jpaTasksRepository.findByBacklogIdOrderByOrderInBacklogAsc(backlogId))
            .willReturn(new TaskData(){});

        // When
        var result = tasksRepository.list(backlogId);

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue()).isEqualTo(expectedTasks);

        then(jpaTasksRepository)
            .should(times(1))
            .findByBacklogIdOrderByOrderInBacklogAsc(backlogId);
    }
}
