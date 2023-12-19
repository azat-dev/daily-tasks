package com.azatdev.dailytasks.data.dao.data;

import java.util.Optional;

import com.azatdev.dailytasks.data.dao.persistence.jpa.JpaBacklogsRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.GetBacklogByIdDao;
import com.azatdev.dailytasks.domain.models.Backlog;

public final class GetBacklogByIdDaoImpl implements GetBacklogByIdDao {

    private final JpaBacklogsRepository backlogsRepository;
    private final MapBacklogDataToDomain mapBacklogDataToDomain;

    public GetBacklogByIdDaoImpl(
        JpaBacklogsRepository backlogsRepository,
        MapBacklogDataToDomain mapBacklogDataToDomain
    ) {
        this.backlogsRepository = backlogsRepository;
        this.mapBacklogDataToDomain = mapBacklogDataToDomain;
    }

    @Override
    public Optional<Backlog> execute(long backlogId) {

        return backlogsRepository.findById(backlogId)
            .map(mapBacklogDataToDomain::map);
    }
}
