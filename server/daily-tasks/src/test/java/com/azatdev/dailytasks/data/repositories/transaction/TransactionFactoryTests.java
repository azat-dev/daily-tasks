package com.azatdev.dailytasks.data.repositories.transaction;

import static org.mockito.BDDMockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;

import com.azatdev.dailytasks.data.repositories.data.TransactionImpl;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;

class TransactionImplFactory implements TransactionFactory {

    private final PlatformTransactionManager transactionManager;

    public TransactionImplFactory(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Transaction make() {
        return null;
        // return new TransactionImpl(transactionManager);
    }
}

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
