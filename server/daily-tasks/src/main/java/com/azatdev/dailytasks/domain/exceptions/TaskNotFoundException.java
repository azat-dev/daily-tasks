package com.azatdev.dailytasks.domain.exceptions;

public class TaskNotFoundException extends Exception {

    private long taskId;

    public TaskNotFoundException(long taskId) {
        super("Task with id " + taskId + " not found");
        this.taskId = taskId;
    }

    public long getTaskId() {
        return taskId;
    }
}
