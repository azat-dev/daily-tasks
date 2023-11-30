package com.azatdev.dailytasks.presentation.api.rest.task;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import com.jayway.jsonpath.JsonPath;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.TestDomainDataGenerator;
import com.azatdev.dailytasks.presentation.api.rest.entities.CreateTaskInBacklogRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskPriorityPresentation;
import com.azatdev.dailytasks.utils.Result;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ListTasksInBacklogUseCase listTasksInBacklogUseCase;

    @MockBean
    private CreateTaskInBacklogUseCase createTaskInBacklogUseCase;

    @Test
    void findAllTasksInBacklogShouldReturnTasksTest() {

        // Given
        final var url = "/tasks/backlog/WEEK/for/2023-11-11";

        LocalDate startDate = LocalDate.of(
            2023,
            11,
            11
        );

        var backlogDuration = Backlog.Duration.WEEK;

        final var tasks = List.of(
            TestDomainDataGenerator.anyTask(0L),
            TestDomainDataGenerator.anyTask(1L)
        );

        given(
            listTasksInBacklogUseCase.execute(
                any(LocalDate.class),
                any(Backlog.Duration.class)
            )
        ).willReturn(Result.success(tasks));

        // When
        var response = restTemplate.getForEntity(
            url,
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        then(listTasksInBacklogUseCase).should(times(1))
            .execute(
                startDate,
                backlogDuration
            );

        final var responseBody = response.getBody();
        final var context = JsonPath.parse(responseBody);

        context.read(
            "$.length()",
            Integer.class
        )
            .equals(tasks.size());

        final var expectedTaskIds = tasks.stream()
            .map(task -> task.id())
            .toList();

        context.read("$.[*].id")
            .equals(expectedTaskIds);
    }

    @Test
    void createNewTaskInBacklogShouldReturnCreatedTaskTest() {

        // Given
        final var url = "/tasks/backlog/WEEK/for/2023-11-11";

        LocalDate date = LocalDate.of(
            2023,
            11,
            11
        );

        var backlogDuration = Backlog.Duration.WEEK;

        final var newTaskData = new CreateTaskInBacklogRequest(
            "New task title",
            TaskPriorityPresentation.HIGH,
            "Description"
        );

        final var createdTask = TestDomainDataGenerator.anyTask(1L);

        given(
            createTaskInBacklogUseCase.execute(
                eq(date),
                eq(backlogDuration),
                any()
            )
        ).willReturn(Result.success(createdTask));

        // When
        final var response = restTemplate.postForEntity(
            url,
            newTaskData,
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var expectedNewTaskData = new NewTaskData(
            newTaskData.title(),
            Task.Priority.HIGH,
            newTaskData.description()
        );

        then(createTaskInBacklogUseCase).should(times(1))
            .execute(
                eq(date),
                eq(backlogDuration),
                eq(expectedNewTaskData)
            );

        final var responseBody = response.getBody();
        final var context = JsonPath.parse(responseBody);

        context.read("$.id")
            .equals(createdTask.id());
    }
}
