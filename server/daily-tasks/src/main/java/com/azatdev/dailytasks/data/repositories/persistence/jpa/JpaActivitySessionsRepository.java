package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.repositories.persistence.entities.ActivitySessionData;

public interface JpaActivitySessionsRepository extends JpaRepository<ActivitySessionData, Long> {

    Optional<ActivitySessionData> findByOwnerIdAndTaskIdAndFinishedAt(
        UUID ownerId,
        long taskId,
        Optional<ZonedDateTime> finishedAt
    );
}
