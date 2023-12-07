package com.azatdev.dailytasks.data.repositories.persistence.jpa;

@FunctionalInterface
public interface JpaGetReference<T, ID> {
    T execute(ID id);
}
