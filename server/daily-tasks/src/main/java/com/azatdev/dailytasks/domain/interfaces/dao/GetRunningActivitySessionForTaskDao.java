package com.azatdev.dailytasks.domain.interfaces.dao;

import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.domain.models.ActivitySession;

@FunctionalInterface
public interface GetRunningActivitySessionForTaskDao {
    Optional<ActivitySession> execute(
        UUID userId,
        long taskId
    );
}
