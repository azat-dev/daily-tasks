package com.azatdev.dailytasks.data.jpa;

import com.azatdev.dailytasks.data.entities.TaskData;

@FunctionalInterface
public interface JpaGetTaskReference {
    TaskData execute(Long id);
}
