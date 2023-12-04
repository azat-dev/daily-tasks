package com.azatdev.dailytasks.presentation.config.presentation.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azatdev.dailytasks.presentation.security.services.jwt.DateTimeProvider;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTServiceImpl;

@Configuration
public class JwtConfig {

    @Bean
    public JWTService jwtService(
        @Value("${app.security.jwt.secret}") String jwtSecret,
        @Value("${app.security.jwt.accessToken.expirationInMs}") long jwtAccessTokenExpirationInMs,
        @Value("${app.security.jwt.refreshToken.expirationInMs}") long jwtRefreshTokenExpirationInMs

    ) {
        DateTimeProvider dateTimeProvider = () -> new Date().getTime();
        return new JWTServiceImpl(
            jwtSecret,
            jwtAccessTokenExpirationInMs,
            jwtRefreshTokenExpirationInMs,
            dateTimeProvider
        );
    }
}
