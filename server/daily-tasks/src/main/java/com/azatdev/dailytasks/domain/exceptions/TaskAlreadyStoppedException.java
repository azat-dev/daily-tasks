package com.azatdev.dailytasks.domain.exceptions;

public class TaskAlreadyStoppedException extends RuntimeException {

    private long taskId;

    public TaskAlreadyStoppedException(long taskId) {
        super("Task with id " + taskId + " already stopped");
        this.taskId = taskId;
    }

    public long getTaskId() {
        return taskId;
    }
}
