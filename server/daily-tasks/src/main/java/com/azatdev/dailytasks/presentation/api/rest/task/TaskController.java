package com.azatdev.dailytasks.presentation.api.rest.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.presentation.api.rest.entities.TaskResponse;

@Controller
public class TaskController implements TaskResource {

    @Override
    public ResponseEntity<List<TaskResponse>> findAllTasksInBacklog(
        Backlog.Duration backlogDuration,
        LocalDate date
    ) {
        // TODO Auto-generated method stub
        return null;
    }
}
