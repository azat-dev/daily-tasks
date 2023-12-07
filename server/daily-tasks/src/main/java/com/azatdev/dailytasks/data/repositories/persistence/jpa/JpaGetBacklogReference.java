package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;

@FunctionalInterface
public interface JpaGetBacklogReference {
    BacklogData execute(Long id);
}
