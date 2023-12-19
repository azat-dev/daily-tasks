package com.azatdev.dailytasks.data.jpa;

import com.azatdev.dailytasks.data.entities.BacklogData;

@FunctionalInterface
public interface JpaGetBacklogReference {
    BacklogData execute(Long id);
}
