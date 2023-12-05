package com.azatdev.dailytasks.domain.interfaces.repositories.backlog;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.azatdev.dailytasks.domain.models.Backlog;

@Repository
public interface BacklogRepositoryGet {

    public Optional<Long> getBacklogId(
        LocalDate startDate,
        Backlog.Duration duration
    );
}
