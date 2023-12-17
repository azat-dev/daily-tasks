package com.azatdev.dailytasks.domain.exceptions;

import java.util.UUID;

public class AccessDeniedException extends RuntimeException {

    private String resource;
    private String operation;
    private UUID userId;

    public AccessDeniedException(
        UUID userId,
        String operation,
        String resource
    ) {
        super("Access denied to " + resource + " for operation " + operation + " for user " + userId);
        this.resource = resource;
        this.operation = operation;
        this.userId = userId;
    }

    public String getResource() {
        return resource;
    }

    public String getOperation() {
        return operation;
    }

    public UUID getUserId() {
        return userId;
    }
}
