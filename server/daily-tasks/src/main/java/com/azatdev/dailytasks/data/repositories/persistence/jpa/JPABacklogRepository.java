package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;

public interface JPABacklogRepository extends JpaRepository<BacklogData, Long> {

    BacklogData findByStartDateAndDuration(LocalDate startDate, BacklogData.Duration duration);
}