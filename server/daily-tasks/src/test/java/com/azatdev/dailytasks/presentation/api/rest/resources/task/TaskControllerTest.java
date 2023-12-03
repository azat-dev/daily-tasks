package com.azatdev.dailytasks.presentation.api.rest.resources.task;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.TestDomainDataGenerator;
import com.azatdev.dailytasks.presentation.api.rest.entities.CreateTaskInBacklogRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskPriorityPresentation;
import com.azatdev.dailytasks.presentation.config.PresentationConfig;
import com.azatdev.dailytasks.utils.Result;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ PresentationConfig.class })
class TaskControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListTasksInBacklogUseCase listTasksInBacklogUseCase;

    @MockBean
    private CreateTaskInBacklogUseCase createTaskInBacklogUseCase;

    @Test
    void findAllTasksInBacklog_givenExistingTasks_thenReturnAllTasksInBacklog() throws Exception {

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
        final var action = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

        // Then

        action.andExpect(status().isOk());

        then(listTasksInBacklogUseCase).should(times(1))
            .execute(
                startDate,
                backlogDuration
            );

        action.andExpect(jsonPath("$.length()").value(tasks.size()));

        final var expectedTaskIds = tasks.stream()
            .map(
                task -> task.id()
                    .intValue()
            )
            .toArray();

        action.andExpect(jsonPath("$[*].id").isArray());
        action.andExpect(
            jsonPath(
                "$[*].id"

            ).value(Matchers.contains(expectedTaskIds))
        );

    }

    @Test
    void createNewTaskInBacklog_givenValidTaskData_thenCreateTask() throws Exception {

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
        final var action = mockMvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTaskData))
        );

        // Then
        action.andExpect(status().isCreated());

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

        action.andExpect(jsonPath("$.id").value(createdTask.id()));
    }
}
