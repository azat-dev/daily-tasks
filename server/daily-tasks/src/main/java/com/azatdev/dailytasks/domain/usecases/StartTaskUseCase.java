package com.azatdev.dailytasks.domain.usecases;

import java.time.ZonedDateTime;
import java.util.UUID;

@FunctionalInterface
public interface StartTaskUseCase {
    // Methods
    ZonedDateTime execute(
        UUID userId,
        Long taskId
    );
}
