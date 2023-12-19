package com.azatdev.dailytasks.data.dao.persistence.jpa;

import com.azatdev.dailytasks.data.dao.persistence.entities.BacklogData;

@FunctionalInterface
public interface JpaGetBacklogReference {
    BacklogData execute(Long id);
}
