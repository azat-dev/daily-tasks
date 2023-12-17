package com.azatdev.dailytasks.data.repositories.persistence.entities;

import java.time.ZonedDateTime;
import java.util.Optional;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Entity
@Table(name = "activity_sessions")
public class ActivitySessionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private @Nonnull UserData owner;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private @Nonnull TaskData task;

    private @Nonnull ZonedDateTime startedAt;

    private ZonedDateTime finishedAt;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    @PrePersist
    public void preCreate() {
        final var now = ZonedDateTime.now();
        this.createdAt = now;
        this.updatedAt = this.createdAt;
    }

    public ActivitySessionData(
        @Nonnull UserData owner,
        @Nonnull TaskData task,
        @Nonnull ZonedDateTime startedAt,
        ZonedDateTime finishedAt
    ) {
        this.owner = owner;
        this.task = task;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public void setFinishedAt(Optional<ZonedDateTime> finishedAt) {
        this.finishedAt = finishedAt.orElse(null);
    }
}
