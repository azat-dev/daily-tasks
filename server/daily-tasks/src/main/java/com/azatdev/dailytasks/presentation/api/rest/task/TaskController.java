package com.azatdev.dailytasks.presentation.api.rest.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.utils.MapTaskToResponse;

@Controller
public class TaskController implements TaskResource {

    private final ListTasksInBacklogUseCase listTasksInBacklogUseCase;
    private final MapTaskToResponse mapTaskToResponse;

    public TaskController(
        ListTasksInBacklogUseCase listTasksInBacklogUseCase,
        MapTaskToResponse mapTaskToResponse
    ) {
        this.listTasksInBacklogUseCase = listTasksInBacklogUseCase;
        this.mapTaskToResponse = mapTaskToResponse;
    }

    @Override
    public ResponseEntity<List<TaskResponse>> findAllTasksInBacklog(
        Backlog.Duration backlogDuration,
        LocalDate date
    ) {
        final var result = listTasksInBacklogUseCase.execute(
            date,
            backlogDuration
        );

        if (!result.isSuccess()) {
            return ResponseEntity.internalServerError()
                .build();
        }

        final var ouputItems = result.getValue()
            .stream()
            .map(mapTaskToResponse::map)
            .toList();

        return ResponseEntity.ok(ouputItems);
    }
}
