package com.azatdev.dailytasks.data.repositories;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;

import jakarta.annotation.Nonnull;

class TransactionImpl implements Transaction {

    private final TransactionManager transactionManager;

    public TransactionImpl(@Nonnull TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void begin() {

    }

    @Override
    public void commit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollback() {
        // TODO Auto-generated method stub

    }
}

class TransactionImplTests {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        // Create a mock TransactionManager
        transactionManager = mock(PlatformTransactionManager.class);

        // Create a TransactionImpl instance with the mock TransactionManager
        transaction = new TransactionImpl(transactionManager);
    }

    @Test
    void commitWithoutBeginTest() {
        // Use assertThrows to check that committing without beginning throws an exception
        assertThrows(
            IllegalStateException.class,
            () -> {
                transaction.commit();
            }
        );

        // Verify that the commit method of the mock TransactionManager was not called
        then(transactionManager).should(never())
            .commit(any());
    }

    @Test
    void rollbackWithoutBeginTest() {
        // Use assertThrows to check that rolling back without beginning throws an exception
        assertThrows(
            IllegalStateException.class,
            () -> {
                transaction.rollback();
            }
        );

        // Verify that the rollback method of the mock TransactionManager was not called
        then(transactionManager).should(never())
            .rollback(any());
    }

    @Test
    void beginShouldStartTransactionTest() {

        // Then
        then(transactionManager).should(times(1))
            .getTransaction(isA(DefaultTransactionDefinition.class));
    }

    @Test
    void commitAfterBeginShouldCommitTransactionTest() {
        // Given
        transaction.begin();

        final var transactionStatus = mock(TransactionStatus.class);

        given(transactionManager.getTransaction(any())).willReturn(transactionStatus);

        // When
        transaction.commit();

        // Then
        then(transactionManager).should(times(1))
            .commit(isA(TransactionStatus.class));
    }

    @Test
    void rollbackAfterBeginShouldRollbackTransactionTest() {
        // Given
        transaction.begin();

        // When
        transaction.rollback();

        // Then
        then(transactionManager).should(times(1))
            .rollback(isA(TransactionStatus.class));
    }
}
