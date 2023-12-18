package com.azatdev.dailytasks.domain.interfaces.dao;

import java.util.Optional;

import com.azatdev.dailytasks.domain.models.Backlog;

@FunctionalInterface
public interface GetBacklogByIdDao {

    Optional<Backlog> execute(long backlogId);
}
