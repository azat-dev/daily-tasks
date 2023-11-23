package com.azatdev.dailytasks.domain.usecases.utils;

import java.time.LocalDate;

import com.azatdev.dailytasks.domain.models.Backlog;


@FunctionalInterface
public interface AdjustDateToStartOfBacklog {
    public LocalDate calculate(LocalDate date, Backlog.Duration duration);
}
