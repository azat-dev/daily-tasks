package com.azatdev.dailytasks.domain.interfaces.dao;

@FunctionalInterface
public interface DeleteAllActivitySessionsOfTaskDao {

    void execute(long taskId);
}
