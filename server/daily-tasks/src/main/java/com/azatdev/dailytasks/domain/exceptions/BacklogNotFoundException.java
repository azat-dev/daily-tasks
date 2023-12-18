package com.azatdev.dailytasks.domain.exceptions;

public class BacklogNotFoundException extends Exception {

    private long backlogId;

    public BacklogNotFoundException(long backlogId) {
        super("Backlog with id " + backlogId + " not found");
        this.backlogId = backlogId;
    }

    public long getBacklogId() {
        return backlogId;
    }
}
