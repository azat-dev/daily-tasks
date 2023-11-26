package com.azatdev.dailytasks.data.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;
import com.azatdev.dailytasks.domain.interfaces.repositories.tasks.TasksRepositoryList;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.utils.Result;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    return Result.success(new Task[] {});
  }
}

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class TasksRepositoryTests {

  // Fields

  @Mock JPATasksRepository jpaTasksRepository;

  @InjectMocks TasksRepositoryImpl tasksRepository;

  // Helpers

  private Long anyBacklogId() {
    return 111L;
  }

  // Tests

  @Test
  void listTasksReturnsShouldReturnTasksFromRepositoryTest() {

    // Given
    final var backlogId = anyBacklogId();
    final String[] expectedTaskTitles = {"1", "2", "3"};

    given(jpaTasksRepository.findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId))
        .willReturn(null);

    // When
    var result = tasksRepository.list(backlogId);

    // Then
    assertThat(result.isSuccess()).isTrue();

    var receivedTasks = result.getValue();
    var receivedTaskTitles =
        Arrays.stream(receivedTasks).map(task -> task.title()).toArray(String[]::new);

    assertThat(receivedTaskTitles).isEqualTo(expectedTaskTitles);

    then(jpaTasksRepository).should(times(1)).findAllByBacklogIdOrderByOrderInBacklogAsc(backlogId);

    assertThat(1).isEqualTo(5);
  }
}
