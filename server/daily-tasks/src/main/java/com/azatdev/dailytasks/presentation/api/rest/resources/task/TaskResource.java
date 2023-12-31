package com.azatdev.dailytasks.presentation.api.rest.resources.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azatdev.dailytasks.presentation.api.rest.entities.BacklogDurationPresentation;
import com.azatdev.dailytasks.presentation.api.rest.entities.CreateTaskInBacklogRequest;
import com.azatdev.dailytasks.presentation.api.rest.entities.StartTaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.StopTaskResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/with-auth/tasks")
public interface TaskResource {

    @GetMapping("/backlog/{backlogDuration}/for/{date}")
    public ResponseEntity<List<TaskResponse>> findAllTasksInBacklog(
        @PathVariable BacklogDurationPresentation backlogDuration,
        @PathVariable LocalDate date,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @PostMapping("/backlog/{backlogDuration}/for/{date}")
    public ResponseEntity<TaskResponse> createTaskInBacklog(
        @PathVariable BacklogDurationPresentation backlogDuration,
        @PathVariable LocalDate date,
        @Valid @RequestBody CreateTaskInBacklogRequest request,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @PostMapping("/{taskId}/start")
    public ResponseEntity<StartTaskResponse> startTask(
        @PathVariable(required = true) Long taskId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(
        @PathVariable(required = true) Long taskId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @PostMapping("/{taskId}/stop")
    public ResponseEntity<StopTaskResponse> stopTask(
        @PathVariable(required = true) Long taskId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    );

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
        @PathVariable(required = true) Long taskId,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    );
}
