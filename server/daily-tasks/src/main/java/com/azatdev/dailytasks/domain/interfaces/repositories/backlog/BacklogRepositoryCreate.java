package com.azatdev.dailytasks.domain.interfaces.repositories.backlog;

import java.time.LocalDate;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.models.Backlog;

import jakarta.annotation.Nonnull;

public interface BacklogRepositoryCreate {

    public long create(
        @Nonnull LocalDate startDate,
        @Nonnull Backlog.Duration duration,
        Transaction transaction
    ) throws CreateException;

    // Exceptions

    public abstract class CreateException extends RuntimeException {
        public CreateException(Throwable cause) {
            super(cause);
        }
    }

    public class InternalErrorException extends CreateException {
        public InternalErrorException(Throwable cause) {
            super(cause);
        }
    }

    public class BacklogAlreadyExistsException extends CreateException {
        public BacklogAlreadyExistsException() {
            super(null);
        }
    }
}
