package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import java.time.LocalDate;
import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;

public interface JPABacklogRepository extends JpaRepository<BacklogData, Long> {

    interface BacklogIdProjection {
        Long getId();
    }

    Optional<BacklogIdProjection> findByOwnerIdAndStartDateAndDuration(
        UUID ownerId,
        LocalDate startDate,
        BacklogData.Duration duration
    );
}
