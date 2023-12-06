package com.azatdev.dailytasks.data.repositories.persistence.backlog;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;

import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JPABacklogRepository;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.backlog.BacklogRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.Backlog;

public class BacklogRepositoryImpl implements BacklogRepository {

    private final JpaUsersRepository jpaUsersRepository;
    private final JPABacklogRepository jpaBacklogRepository;

    public BacklogRepositoryImpl(
        JpaUsersRepository jpaUsersRepository,
        JPABacklogRepository jpaBacklogRepository
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
            final var backlogData = jpaBacklogRepository.findByOwnerIdAndStartDateAndDuration(
                ownerId,
                startDate,
                this.mapDuration(duration)
            );

            if (backlogData != null) {
                return Optional.of(backlogData.getId());
            }

            return Optional.empty();
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

        try {

            final var result = this.jpaBacklogRepository.saveAndFlush(
                new BacklogData(
                    jpaUsersRepository.getReferenceById(ownerId),
                    startDate,
                    this.mapDuration(duration)
                )
            );
            return result.getId();

        } catch (DataIntegrityViolationException e) {
            throw new UnsupportedOperationException("Not implemented yet");

        }
    }
}
