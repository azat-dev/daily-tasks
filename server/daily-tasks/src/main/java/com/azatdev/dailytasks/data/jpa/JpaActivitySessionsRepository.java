package com.azatdev.dailytasks.data.jpa;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.azatdev.dailytasks.data.entities.ActivitySessionData;

public interface JpaActivitySessionsRepository extends JpaRepository<ActivitySessionData, Long> {

    Optional<ActivitySessionData> findByOwnerIdAndTaskIdAndFinishedAt(
        UUID ownerId,
        long taskId,
        Optional<ZonedDateTime> finishedAt
    );

    @Modifying
    @Query("DELETE FROM ActivitySessionData a WHERE a.taskId = :taskId")
    void deleteAllByTaskId(@Param("taskId") long taskId);
}
