package com.azatdev.dailytasks.data.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.entities.TaskData;

public interface JpaTasksRepository extends JpaRepository<TaskData, Long> {
    interface OrderInBacklogProjection {
        Integer getOrderInBacklog();
    }

    List<TaskData> findAllByOwnerIdAndBacklogIdOrderByOrderInBacklogAsc(
        UUID ownerId,
        Long backlogId
    );

    Optional<OrderInBacklogProjection> findFirstOrderInBacklogByOwnerIdAndBacklogIdOrderByOrderInBacklogDesc(
        UUID ownerId,
        Long backlogId
    );

    Optional<TaskData> findByOwnerIdAndId(
        UUID ownerId,
        Long id
    );
}
