package com.azatdev.dailytasks.presentation.api.rest.resources.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Backlog.Duration;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.presentation.api.rest.entities.CreateTaskInBacklogRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.utils.MapTaskToResponse;

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
        Backlog.Duration backlogDuration,
        LocalDate date
    ) {
        final var tasksInBacklog = listTasksInBacklogUseCase.execute(
            date,
            backlogDuration
        );

        final var ouputItems = tasksInBacklog.stream()
            .map(mapTaskToResponse::map)
            .toList();

        return ResponseEntity.ok(ouputItems);
    }

    @Override
    public ResponseEntity<TaskResponse> createTaskInBacklog(
        Duration backlogDuration,
        LocalDate date,
        CreateTaskInBacklogRequest request
    ) {

        final var newTaskData = new NewTaskData(
            request.title(),
            request.priority()
                .toDomain(),
            request.description()
        );

        final var createdTask = createTaskInBacklogUseCase.execute(
            date,
            backlogDuration,
            newTaskData
        );

        final var mappedTask = mapTaskToResponse.map(createdTask);
        return ResponseEntity.created(null)
            .body(mappedTask);
    }
}
