package com.azatdev.dailytasks.data.repositories.data.transaction;

import org.springframework.transaction.PlatformTransactionManager;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;
import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.TransactionFactory;

public class TransactionImplFactory implements TransactionFactory {

    private final PlatformTransactionManager transactionManager;

    public TransactionImplFactory(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Transaction make() {
        return new TransactionImpl(transactionManager);
    }
}
