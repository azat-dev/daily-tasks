package com.azatdev.dailytasks.data.repositories.transaction;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;

import com.azatdev.dailytasks.data.dao.transaction.TransactionImplFactory;

class TransactionFactoryTests {

    @Test
    void makeTest() {

        // Given
        final var transactionManager = mock(PlatformTransactionManager.class);
        final var transactionFactory = new TransactionImplFactory(transactionManager);

        // When
        final var transaction = transactionFactory.make();
        transaction.begin();

        // Then
        then(transactionManager).should(times(1))
            .getTransaction(any());
    }
}
