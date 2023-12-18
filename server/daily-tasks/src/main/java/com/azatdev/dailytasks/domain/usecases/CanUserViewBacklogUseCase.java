package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

import com.azatdev.dailytasks.domain.exceptions.BacklogNotFoundException;

public interface CanUserViewBacklogUseCase {
    boolean execute(
        UUID userId,
        long backlogId
    ) throws BacklogNotFoundException;
}
