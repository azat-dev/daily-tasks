package com.azatdev.dailytasks.presentation.api.rest.resources.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.presentation.api.rest.entities.BacklogDurationPresentation;
import com.azatdev.dailytasks.presentation.api.rest.entities.CreateTaskInBacklogRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.StartTaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskPriorityPresentation;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.utils.MapTaskToResponse;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;

@Controller
public class TaskController implements TaskResource {

    private final ListTasksInBacklogUseCase listTasksInBacklogUseCase;
    private final CreateTaskInBacklogUseCase createTaskInBacklogUseCase;
    private final MapTaskToResponse mapTaskToResponse;

    public TaskController(
        ListTasksInBacklogUseCase listTasksInBacklogUseCase,
        CreateTaskInBacklogUseCase createTaskInBacklogUseCase,
        MapTaskToResponse mapTaskToResponse
    ) {
        this.listTasksInBacklogUseCase = listTasksInBacklogUseCase;
        this.createTaskInBacklogUseCase = createTaskInBacklogUseCase;
        this.mapTaskToResponse = mapTaskToResponse;
    }

    @Override
    public ResponseEntity<List<TaskResponse>> findAllTasksInBacklog(
        BacklogDurationPresentation backlogDuration,
        LocalDate date,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        final var tasksInBacklog = listTasksInBacklogUseCase.execute(
            userPrincipal.getId(),
            date,
            backlogDuration.toDomain()
        );

        final var ouputItems = tasksInBacklog.stream()
            .map(mapTaskToResponse::map)
            .toList();

        return ResponseEntity.ok(ouputItems);
    }

    @Override
    public ResponseEntity<TaskResponse> createTaskInBacklog(
        BacklogDurationPresentation backlogDuration,
        LocalDate date,
        CreateTaskInBacklogRequest request,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        final var newTaskData = new NewTaskData(
            request.title(),
            request.priority()
                .map(TaskPriorityPresentation::toDomain),
            request.description()
        );

        final var createdTask = createTaskInBacklogUseCase.execute(
            userPrincipal.getId(),
            date,
            backlogDuration.toDomain(),
            newTaskData
        );

        final var mappedTask = mapTaskToResponse.map(createdTask);

        return ResponseEntity.created(null)
            .body(mappedTask);
    }

    @Override
    public  ResponseEntity<StartTaskResponse> startTask(
        Long taskId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.badRequest().build();
    }
}
