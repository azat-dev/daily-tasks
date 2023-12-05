package com.azatdev.dailytasks.domain.interfaces.repositories.backlog;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.azatdev.dailytasks.domain.models.Backlog;

@Repository
public interface BacklogRepositoryGet {

    public Optional<Long> getBacklogId(
        UUID userId,
        LocalDate startDate,
        Backlog.Duration duration
    );
}
