package com.azatdev.dailytasks.data.repositories.persistence.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "backlogs", uniqueConstraints = { @UniqueConstraint(columnNames = { "start_date", "duration" }) })
public class BacklogData {

    // Types

    public enum Duration {
        DAY,
        WEEK
    }

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Duration duration;

    // Constructors

    public BacklogData(
        LocalDate startDate,
        Duration duration
    ) {
        this.startDate = startDate;
        this.duration = duration;
    }

    // Getters

    public Duration getDuration() {
        return duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Long getId() {
        return id;
    }
}
