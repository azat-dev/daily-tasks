package com.azatdev.dailytasks.domain.interfaces.repositories.transaction;

public interface Transaction {

    public void begin();

    public void commit();

    public void rollback();
}
