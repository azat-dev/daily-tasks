package com.azatdev.dailytasks.presentation.api.rest.resources.task;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.azatdev.dailytasks.domain.models.Backlog;

@Component
public class StringToBacklogTypeConverter
  implements Converter<String, Backlog.Duration> {

    @Override
    public Backlog.Duration convert(String source) {
        return Backlog.Duration.valueOf(source.toUpperCase());
    }
}