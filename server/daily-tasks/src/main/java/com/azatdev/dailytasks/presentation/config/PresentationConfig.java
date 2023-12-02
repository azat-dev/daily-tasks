package com.azatdev.dailytasks.presentation.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azatdev.dailytasks.data.repositories.data.UsersRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsService;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsServiceImpl;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTServiceImpl;

@Configuration
public class PresentationConfig {

    @Value("${app.security.jwt.secret}")
    String jwtSecret;

    @Value("${app.security.jwt.accessToken.expirationInMs}")
    int jwtAccessTokenExpirationInMs;

    @Value("${app.security.jwt.refreshToken.expirationInMs}")
    int jwtRefreshTokenExpirationInMs;

    @Bean
    public UsersRepository usersRepository(JpaUsersRepository jpaUsersRepository) {
        return new UsersRepositoryImpl(jpaUsersRepository);
    }

    @Bean
    public CustomUserDetailsService userDetailsService(UsersRepository usersRepository) {
        return new CustomUserDetailsServiceImpl(usersRepository);
    }

    @Bean
    public JWTService jwtService() {
        return new JWTServiceImpl(
            jwtSecret,
            jwtAccessTokenExpirationInMs,
            jwtRefreshTokenExpirationInMs,
            () -> new Date().getTime()
        );
    }
}
