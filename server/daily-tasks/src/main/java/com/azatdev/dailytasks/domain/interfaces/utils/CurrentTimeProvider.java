package com.azatdev.dailytasks.domain.interfaces.utils;

import java.time.ZonedDateTime;

@FunctionalInterface
public interface CurrentTimeProvider {
    ZonedDateTime execute();
}
