package com.azatdev.dailytasks.presentation.api.rest.resources.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.azatdev.dailytasks.domain.exceptions.AccessDeniedException;
import com.azatdev.dailytasks.domain.exceptions.TaskAlreadyStoppedException;
import com.azatdev.dailytasks.domain.exceptions.TaskNotFoundException;
import com.azatdev.dailytasks.domain.models.NewTaskData;
import com.azatdev.dailytasks.domain.usecases.CreateTaskInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.DeleteTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.GetTaskDetailsUseCase;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.StartTaskUseCase;
import com.azatdev.dailytasks.domain.usecases.StopTaskUseCase;
import com.azatdev.dailytasks.presentation.api.rest.entities.BacklogDurationPresentation;
import com.azatdev.dailytasks.presentation.api.rest.entities.CreateTaskInBacklogRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.StartTaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.StopTaskResponse;
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
    private StopTaskUseCase stopTaskUseCase;

    @Autowired
    private GetTaskDetailsUseCase getTaskDetailsUseCase;

    @Autowired
    private DeleteTaskUseCase deleteTaskUseCase;

    @Override
    public ResponseEntity<List<TaskResponse>> findAllTasksInBacklog(
        BacklogDurationPresentation backlogDuration,
        LocalDate date,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        try {
            final var tasksInBacklog = listTasksInBacklogUseCase.execute(
                userPrincipal.getId(),
                date,
                backlogDuration.toDomain()
            );

            final var ouputItems = tasksInBacklog.stream()
                .map(mapTaskToResponse::map)
                .toList();

            return ResponseEntity.ok(ouputItems);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
        }
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
        try {
            final var startedAt = this.startTaskUseCase.execute(
                userPrincipal.getId(),
                taskId
            );

            return ResponseEntity.ok(new StartTaskResponse(startedAt));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound()
                .build();
        }
    }

    @Override
    public ResponseEntity<TaskResponse> getTask(
        Long taskId,
        UserPrincipal userPrincipal
    ) {
        try {
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

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
        }
    }

    @Override
    public ResponseEntity<StopTaskResponse> stopTask(
        Long taskId,
        UserPrincipal userPrincipal
    ) {

        try {
            final var stoppedAt = this.stopTaskUseCase.execute(
                userPrincipal.getId(),
                taskId
            );

            return ResponseEntity.ok(new StopTaskResponse(stoppedAt));

        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound()
                .build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
        } catch (TaskAlreadyStoppedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteTask(
        Long taskId,
        UserPrincipal userPrincipal
    ) {

        try {
            this.deleteTaskUseCase.execute(
                userPrincipal.getId(),
                taskId
            );

            return ResponseEntity.noContent()
                .build();

        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound()
                .build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
        }
    }
}
