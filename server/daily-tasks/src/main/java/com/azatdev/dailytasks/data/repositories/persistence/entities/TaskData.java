package com.azatdev.dailytasks.data.repositories.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;

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

  @ManyToOne(optional = false, targetEntity = BacklogData.class)
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
      Priority priority) {
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
}
