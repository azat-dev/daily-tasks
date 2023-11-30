package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;

public interface JPATasksRepository extends JpaRepository<TaskData, Long> {
    public interface OrderInBacklogProjection {

        Integer getOrderInBacklog();
    }

    public Iterable<TaskData> findAllByBacklogIdOrderByOrderInBacklogAsc(Long backlogId);

    public Optional<OrderInBacklogProjection> findFirstOrderInBacklogByBacklogIdOrderByOrderInBacklogDesc(
        Long backlogId
    );
}
