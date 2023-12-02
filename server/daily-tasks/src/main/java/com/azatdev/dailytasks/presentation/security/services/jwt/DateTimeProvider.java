package com.azatdev.dailytasks.presentation.security.services.jwt;

@FunctionalInterface
public interface DateTimeProvider {

    public long now();
}
