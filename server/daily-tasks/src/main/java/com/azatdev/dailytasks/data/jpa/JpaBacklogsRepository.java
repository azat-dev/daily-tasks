package com.azatdev.dailytasks.data.jpa;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.entities.BacklogData;

public interface JpaBacklogsRepository extends JpaRepository<BacklogData, Long> {

    interface BacklogIdProjection {
        Long getId();
    }

    Optional<BacklogIdProjection> findByOwnerIdAndStartDateAndDuration(
        UUID ownerId,
        LocalDate startDate,
        BacklogData.Duration duration
    );
}
