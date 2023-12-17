package com.azatdev.dailytasks.data.repositories.persistence.entities;

import java.time.ZonedDateTime;

import org.hibernate.validator.constraints.Length;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@Entity
@NoArgsConstructor
@Table(name = "tasks")
public class TaskData {

    // Types

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    // Fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserData owner;

    @ManyToOne
    @JoinColumn(name = "backlog_id", nullable = false)
    private BacklogData backlog;

    @Column(name = "order_in_backlog")
    private Integer orderInBacklog;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 10000)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private Priority priority;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status status;

    // Constructors

    public TaskData(
        UserData owner,
        BacklogData backlog,
        Integer orderInBacklog,
        String title,
        String description,
        Status status,
        Priority priority
    ) {
        this.owner = owner;
        this.backlog = backlog;
        this.orderInBacklog = orderInBacklog;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.description = description;
    }

    @PrePersist
    void prePersist() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
