package com.azatdev.dailytasks.data.repositories.persistence.entities;

import java.time.LocalDate;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Getter
@Table(
    name = "backlogs",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "start_date", "duration", "owner_id" }) }
)
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

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserData owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Duration duration;

    // Constructors

    public BacklogData(
        UserData owner,
        LocalDate startDate,
        Duration duration
    ) {
        this.owner = owner;
        this.startDate = startDate;
        this.duration = duration;
    }
}
