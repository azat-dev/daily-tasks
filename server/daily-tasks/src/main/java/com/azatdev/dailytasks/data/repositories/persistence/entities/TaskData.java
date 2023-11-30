package com.azatdev.dailytasks.data.repositories.persistence.entities;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
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

    // @ManyToOne(optional = false, targetEntity = BacklogData.class)
    @Column(name = "backlog_id", nullable = false)
    private Long backlogId;

    @Column(name = "order_in_backlog")
    private Integer orderInBacklog;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
    private Priority priority;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status status;

    // Constructors

    public TaskData(
        Long backlogId,
        Integer orderInBacklog,
        String title,
        String description,
        Status status,
        Priority priority
    ) {
        this.backlogId = backlogId;
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

    // Accessors

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Long getBacklogId() {
        return backlogId;
    }

    public Integer getOrderInBacklog() {
        return orderInBacklog;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((backlogId == null) ? 0 : backlogId.hashCode());
        result = prime * result + ((orderInBacklog == null) ? 0 : orderInBacklog.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaskData other = (TaskData) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (backlogId == null) {
            if (other.backlogId != null)
                return false;
        } else if (!backlogId.equals(other.backlogId))
            return false;
        if (orderInBacklog == null) {
            if (other.orderInBacklog != null)
                return false;
        } else if (!orderInBacklog.equals(other.orderInBacklog))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        } else if (!updatedAt.equals(other.updatedAt))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (priority != other.priority)
            return false;
        if (status != other.status)
            return false;
        return true;
    }
}
