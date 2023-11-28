package com.azatdev.dailytasks.domain.interfaces.repositories.transaction;

@FunctionalInterface
public interface TransactionFactory {

    public Transaction make();
}
