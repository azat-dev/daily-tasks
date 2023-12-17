package com.azatdev.dailytasks.presentation.api.rest.resources.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.GetTaskDetailsUseCase;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.StartTaskUseCase;
import com.azatdev.dailytasks.presentation.api.rest.entities.BacklogDurationPresentation;
import com.azatdev.dailytasks.presentation.api.rest.entities.CreateTaskInBacklogRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.StartTaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskPriorityPresentation;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.utils.MapTaskToResponse;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;

@Controller
public class TaskController implements TaskResource {

    @Autowired
    private ListTasksInBacklogUseCase listTasksInBacklogUseCase;

    @Autowired
    private CreateTaskInBacklogUseCase createTaskInBacklogUseCase;

    @Autowired
    private MapTaskToResponse mapTaskToResponse;

    @Autowired
    private StartTaskUseCase startTaskUseCase;

    @Autowired
    private GetTaskDetailsUseCase getTaskDetailsUseCase;

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
    public ResponseEntity<StartTaskResponse> startTask(
        Long taskId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        final var startedAt = this.startTaskUseCase.execute(
            userPrincipal.getId(),
            taskId
        );

        return ResponseEntity.ok(new StartTaskResponse(startedAt));
    }

    @Override
    public ResponseEntity<TaskResponse> getTask(
        Long taskId,
        UserPrincipal userPrincipal
    ) {
        final var foundTask = getTaskDetailsUseCase.execute(
            userPrincipal.getId(),
            taskId
        );

        if (foundTask.isEmpty()) {
            return ResponseEntity.notFound()
                .build();
        }

        final var mappedTask = mapTaskToResponse.map(foundTask.get());
        return ResponseEntity.ok(mappedTask);
    }
}
