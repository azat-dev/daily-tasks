package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;

@FunctionalInterface
public interface JpaGetTaskReference {
    TaskData execute(Long id);
}
