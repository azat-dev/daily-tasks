package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;

public interface JPATasksRepository extends JpaRepository<TaskData, Long> {

    public Iterable<TaskData> findAllByBacklogIdOrderByOrderInBacklogAsc(Long backlogId);
}
