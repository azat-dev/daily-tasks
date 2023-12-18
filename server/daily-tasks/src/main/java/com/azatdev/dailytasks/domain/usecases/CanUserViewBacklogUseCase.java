package com.azatdev.dailytasks.domain.usecases;

import java.util.UUID;

public interface CanUserViewBacklogUseCase {
    boolean execute(
        UUID userId,
        long backlogId
    );
}
