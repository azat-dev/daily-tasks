package com.azatdev.dailytasks.data.repositories.data.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.azatdev.dailytasks.domain.interfaces.repositories.transaction.Transaction;

import jakarta.annotation.Nonnull;

public class TransactionImpl implements Transaction {

    private final PlatformTransactionManager transactionManager;

    private TransactionStatus currentTransactionStatus;

    public TransactionImpl(@Nonnull PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void begin() {
        final var definition = new DefaultTransactionDefinition();
        this.currentTransactionStatus = this.transactionManager.getTransaction(definition);
    }

    @Override
    public void commit() {

        if (this.currentTransactionStatus == null) {
            throw new IllegalStateException("Transaction not started");
        }

        this.transactionManager.commit(this.currentTransactionStatus);
    }

    @Override
    public void rollback() {

        if (this.currentTransactionStatus == null) {
            throw new IllegalStateException("Transaction not started");
        }

        this.transactionManager.rollback(this.currentTransactionStatus);
    }
}
