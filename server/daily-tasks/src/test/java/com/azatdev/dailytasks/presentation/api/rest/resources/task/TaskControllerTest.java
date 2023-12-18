package com.azatdev.dailytasks.presentation.api.rest.resources.task;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.DeleteTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.GetTaskDetailsUseCase;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.StartTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.StopTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.TestDomainDataGenerator;
import com.azatdev.dailytasks.presentation.api.rest.entities.CreateTaskInBacklogRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;
import com.azatdev.dailytasks.presentation.config.presentation.PresentationConfig;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;

@WebMvcTest(TaskController.class)
@Import(PresentationConfig.class)
class TaskControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListTasksInBacklogUseCase listTasksInBacklogUseCase;

    @MockBean
    private CreateTaskInBacklogUseCase createTaskInBacklogUseCase;

    @MockBean
    private StartTaskUseCase startTaskUseCase;

    @MockBean
    private GetTaskDetailsUseCase getTaskDetailsUseCase;

    @MockBean
    private StopTaskUseCase stopTaskUseCase;

    @MockBean
    private DeleteTaskUseCase deleteTaskUseCase;

    @Test
    void findAllTasksInBacklog_givenExistingTasks_thenReturnAllTasksInBacklog() throws Exception {

        // Given
        final var url = "/api/with-auth/tasks/backlog/WEEK/for/2023-11-11";

        final var userPrincipal = anyUserPrincipal();
        final var userId = userPrincipal.getId();

        LocalDate startDate = LocalDate.of(
            2023,
            11,
            11
        );

        var backlogDuration = Backlog.Duration.WEEK;

        final var existingTasks = List.of(
            TestDomainDataGenerator.anyTask(
                0L,
                userId
            ),
            TestDomainDataGenerator.anyTask(
                1L,
                userId
            )
        );

        given(
            listTasksInBacklogUseCase.execute(
                eq(userId),
                any(LocalDate.class),
                any(Backlog.Duration.class)
            )
        ).willReturn(existingTasks);

        // When
        final var action = performGet(
            url,
            userPrincipal
        );

        // Then
        action.andExpect(status().isOk());

        then(listTasksInBacklogUseCase).should(times(1))
            .execute(
                userId,
                startDate,
                backlogDuration
            );

        action.andExpect(jsonPath("$.length()").value(existingTasks.size()));

        final var expectedTaskIds = existingTasks.stream()
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

    UserPrincipal anyUserPrincipal() {
        final var mockedUser = mock(UserPrincipal.class);
        given(mockedUser.getId()).willReturn(UUID.randomUUID());

        return mockedUser;
    }

    @Test
    void createNewTaskInBacklog_givenValidTaskData_thenCreateTask() throws Exception {

        // Given
        final var userPrincipal = anyUserPrincipal();
        final var userId = userPrincipal.getId();

        final var url = "/api/with-auth/tasks/backlog/WEEK/for/2023-11-11";

        LocalDate date = LocalDate.of(
            2023,
            11,
            11
        );

        var backlogDuration = Backlog.Duration.WEEK;

        final var newTaskData = new CreateTaskInBacklogRequest(
            "New task title",
            Optional.empty(),
            "Description"
        );

        final var createdTask = TestDomainDataGenerator.anyTask(
            1L,
            userId
        );

        given(
            createTaskInBacklogUseCase.execute(
                eq(userId),
                eq(date),
                eq(backlogDuration),
                any()
            )
        ).willReturn(createdTask);

        // When
        final var action = mockMvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .with(user(userPrincipal))
                .with(csrf())
                .content(objectMapper.writeValueAsString(newTaskData))
        );

        // Then
        action.andExpect(status().isCreated());

        final var expectedNewTaskData = new NewTaskData(
            newTaskData.title(),
            Optional.empty(),
            newTaskData.description()
        );

        then(createTaskInBacklogUseCase).should(times(1))
            .execute(
                userId,
                date,
                backlogDuration,
                expectedNewTaskData
            );

        action.andExpect(jsonPath("$.id").value(createdTask.id()));
    }

    @Test
    void startTask_givenExistingTask_thenStartTask() throws Exception {

        // Given
        final var userPrincipal = anyUserPrincipal();
        final var userId = userPrincipal.getId();
        final var taskId = 1L;

        final var url = "/api/with-auth/tasks/" + taskId + "/start";
        final var startedAt = ZonedDateTime.of(
            2023,
            1,
            2,
            3,
            4,
            5,
            6,
            ZoneId.ofOffset(
                "UTC",
                ZoneOffset.ofHours(3)
            )
        );

        given(
            startTaskUseCase.execute(
                userId,
                taskId
            )
        ).willReturn(startedAt);

        // When
        final var action = mockMvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .with(user(userPrincipal))
                .with(csrf())
        );

        // Then
        action.andExpect(status().isOk());

        then(startTaskUseCase).should(times(1))
            .execute(
                userId,
                taskId
            );

        final var expectedTime = "2023-01-02T03:04:05.000000006+03:00";
        action.andExpect(jsonPath("$.startedAt").value(expectedTime));
    }

    @Test
    void getTask_givenNotExistingTask_thenReturn404() throws Exception {

        // Given
        final var userPrincipal = anyUserPrincipal();
        final var userId = userPrincipal.getId();
        final var taskId = 1L;

        given(
            getTaskDetailsUseCase.execute(
                userId,
                taskId
            )
        ).willReturn(Optional.empty());

        // When
        final var action = performGetTask(
            userPrincipal,
            taskId
        );

        // Then
        action.andExpect(status().isNotFound());
    }

    @Test
    void getTask_givenExistingTask_thenReturnTask() throws Exception {

        // Given
        final var userPrincipal = anyUserPrincipal();
        final var userId = userPrincipal.getId();
        final var taskId = 1L;

        final var existingTask = TestDomainDataGenerator.anyTask(
            taskId,
            userId
        );
        final var mappedTask = mock(TaskResponse.class);

        given(
            getTaskDetailsUseCase.execute(
                userId,
                taskId
            )
        ).willReturn(Optional.of(existingTask));

        given(mappedTask.id()).willReturn(taskId);

        // When
        final var action = performGetTask(
            userPrincipal,
            taskId
        );

        // Then

        then(getTaskDetailsUseCase).should(times(1))
            .execute(
                userId,
                taskId
            );

        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.id").value(taskId));
    }

    @Test
    void stopTask_givenNotExistingTask_thenReturn404() throws Exception {

        // Given
        final var userPrincipal = anyUserPrincipal();
        final var userId = userPrincipal.getId();
        final var taskId = 1L;

        given(
            stopTaskUseCase.execute(
                userId,
                taskId
            )
        ).willThrow(new TaskNotFoundException(taskId));

        // When
        final var action = performStopTask(
            userPrincipal,
            taskId
        );

        // Then
        action.andExpect(status().isNotFound());
    }

    @Test
    void stopTask_givenExistingTask_thenReturnTask() throws Exception {

        // Given
        final var userPrincipal = anyUserPrincipal();
        final var userId = userPrincipal.getId();
        final var taskId = 1L;

        final var mappedTask = mock(TaskResponse.class);

        final var stoppedAt = ZonedDateTime.of(
            2023,
            1,
            2,
            3,
            4,
            5,
            6,
            ZoneId.ofOffset(
                "UTC",
                ZoneOffset.ofHours(3)
            )
        );

        given(
            stopTaskUseCase.execute(
                userId,
                taskId
            )
        ).willReturn(stoppedAt);

        given(mappedTask.id()).willReturn(taskId);

        // When
        final var action = performStopTask(
            userPrincipal,
            taskId
        );

        // Then

        then(stopTaskUseCase).should(times(1))
            .execute(
                userId,
                taskId
            );

        action.andExpect(status().isOk());

        final var expectedTime = "2023-01-02T03:04:05.000000006+03:00";
        action.andExpect(jsonPath("$.stoppedAt").value(expectedTime));
    }

    @Test
    void deleteTask_givenExistingTask_thenDeleteTask() throws Exception {

        // Given
        final var userPrincipal = anyUserPrincipal();
        final var userId = userPrincipal.getId();
        final var taskId = 1L;

        // When
        final var action = performDeleteTask(
            userPrincipal,
            taskId
        );

        // Then
        then(deleteTaskUseCase).should(times(1))
            .execute(
                userId,
                taskId
            );

        action.andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_givenNotExistingTask_thenReturn404() throws Exception {

        // Given
        final var userPrincipal = anyUserPrincipal();
        final var userId = userPrincipal.getId();
        final var taskId = 1L;

        willThrow(new TaskNotFoundException(taskId)).given(deleteTaskUseCase)
            .execute(
                userId,
                taskId
            );

        // When
        final var action = performDeleteTask(
            userPrincipal,
            taskId
        );

        // Then
        then(deleteTaskUseCase).should(times(1))
            .execute(
                userId,
                taskId
            );

        action.andExpect(status().isNotFound());
    }

    private ResultActions performGetTask(
        UserPrincipal userPrincipal,
        long taskId
    ) throws Exception {
        final var url = "/api/with-auth/tasks/" + taskId;

        return performGet(
            url,
            userPrincipal
        );
    }

    private ResultActions performGet(
        String url,
        UserPrincipal userPrincipal
    ) throws Exception {
        return mockMvc.perform(
            get(url).with(user(userPrincipal))
                .with(csrf())
        );
    }

    private ResultActions performDelete(
        String url,
        UserPrincipal userPrincipal
    ) throws Exception {
        return mockMvc.perform(
            delete(url).with(user(userPrincipal))
                .with(csrf())
        );
    }

    private ResultActions performStopTask(
        UserPrincipal userPrincipal,
        long taskId
    ) throws Exception {
        final var url = "/api/with-auth/tasks/" + taskId + "/stop";

        return performPost(
            url,
            userPrincipal
        );
    }

    private ResultActions performDeleteTask(
        UserPrincipal userPrincipal,
        long taskId
    ) throws Exception {
        final var url = "/api/with-auth/tasks/" + taskId;

        return performDelete(
            url,
            userPrincipal
        );
    }

    private ResultActions performPost(
        String url,
        UserPrincipal userPrincipal
    ) throws Exception {
        return mockMvc.perform(
            post(url).with(user(userPrincipal))
                .with(csrf())
        );
    }
}
