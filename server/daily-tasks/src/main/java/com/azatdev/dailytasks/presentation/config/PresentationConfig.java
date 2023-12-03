package com.azatdev.dailytasks.presentation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azatdev.dailytasks.presentation.api.rest.entities.utils.MapTaskToResponse;
import com.azatdev.dailytasks.presentation.api.rest.entities.utils.MapTaskToResponseImpl;

@Configuration
public class PresentationConfig {

    @Bean
    public MapTaskToResponse mapTaskToResponse() {
        return new MapTaskToResponseImpl();
    }
}
