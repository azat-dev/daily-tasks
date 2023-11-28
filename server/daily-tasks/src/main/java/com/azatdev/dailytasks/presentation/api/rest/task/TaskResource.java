package com.azatdev.dailytasks.presentation.api.rest.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;

@RestController
@RequestMapping("/tasks")
public interface TaskResource {

    @GetMapping("/backlog/{backlogDuration}/for/{date}")
    public ResponseEntity<List<TaskResponse>> findAllTasksInBacklog(
        @PathVariable Backlog.Duration backlogDuration,
        @PathVariable LocalDate date
    );
}
