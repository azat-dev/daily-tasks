package com.azatdev.dailytasks.data.dao.persistence.jpa;

import com.azatdev.dailytasks.data.dao.persistence.entities.TaskData;

@FunctionalInterface
public interface JpaGetTaskReference {
    TaskData execute(Long id);
}
