package com.azatdev.dailytasks.data.dao.activitysession;

import com.azatdev.dailytasks.data.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.DeleteAllActivitySessionsOfTaskDao;

public final class DeleteAllActivitySessionsOfTaskDaoImpl implements DeleteAllActivitySessionsOfTaskDao {

    private final JpaActivitySessionsRepository jpaActivitySessionsRepository;

    public DeleteAllActivitySessionsOfTaskDaoImpl(JpaActivitySessionsRepository jpaActivitySessionsRepository) {
        this.jpaActivitySessionsRepository = jpaActivitySessionsRepository;
    }

    @Override
    public void execute(long taskId) {
        throw new UnsupportedOperationException();
    }
}
