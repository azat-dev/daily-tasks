package com.azatdev.dailytasks.data.jpa;

@FunctionalInterface
public interface JpaGetReference<T, ID> {
    T execute(ID id);
}
