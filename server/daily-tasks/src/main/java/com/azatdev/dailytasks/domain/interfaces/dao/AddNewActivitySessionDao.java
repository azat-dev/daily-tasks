package com.azatdev.dailytasks.domain.interfaces.dao;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.models.ActivitySession;

@FunctionalInterface
public interface AddNewActivitySessionDao {
    Optional<ActivitySession> execute(
        UUID userId,
        long taskId,
        ZonedDateTime startedAt,
        Optional<ZonedDateTime> finishedAt
    );
}
