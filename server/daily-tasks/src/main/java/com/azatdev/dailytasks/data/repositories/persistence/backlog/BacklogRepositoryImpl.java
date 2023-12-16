package com.azatdev.dailytasks.data.repositories.persistence.backlog;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaBacklogsRepository;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.Backlog;

public class BacklogRepositoryImpl implements BacklogRepository {

    private final JpaUsersRepository jpaUsersRepository;
    private final JpaBacklogsRepository jpaBacklogRepository;

    public BacklogRepositoryImpl(
        JpaUsersRepository jpaUsersRepository,
        JpaBacklogsRepository jpaBacklogRepository
    ) {
        this.jpaUsersRepository = jpaUsersRepository;
        this.jpaBacklogRepository = jpaBacklogRepository;
    }

    private BacklogData.Duration mapDuration(Backlog.Duration duration) {

        switch (duration) {
        case DAY:
            return BacklogData.Duration.DAY;
        case WEEK:
            return BacklogData.Duration.WEEK;
        default:
            throw new IllegalArgumentException("Unknown duration");
        }
    }

    @Override
    public Optional<Long> getBacklogId(
        UUID ownerId,
        LocalDate startDate,
        Backlog.Duration duration
    ) {

        try {
            final var backlogIdProjection = jpaBacklogRepository.findByOwnerIdAndStartDateAndDuration(
                ownerId,
                startDate,
                this.mapDuration(duration)
            );

            return backlogIdProjection.map(JpaBacklogsRepository.BacklogIdProjection::getId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long create(
        UUID ownerId,
        LocalDate startDate,
        Backlog.Duration duration,
        Optional<Transaction> transaction
    ) throws BacklogAlreadyExistsException {

        final var mappedDuration = this.mapDuration(duration);

        final var backlogIdProjection = jpaBacklogRepository.findByOwnerIdAndStartDateAndDuration(
            ownerId,
            startDate,
            mappedDuration
        );

        if (backlogIdProjection.isPresent()) {
            final var backlogId = backlogIdProjection.get()
                .getId();

            throw new BacklogAlreadyExistsException(backlogId);
        }

        final var data = new BacklogData(
            jpaUsersRepository.getReferenceById(ownerId),
            startDate,
            mappedDuration
        );

        final var result = this.jpaBacklogRepository.saveAndFlush(data);
        return result.getId();
    }
}
