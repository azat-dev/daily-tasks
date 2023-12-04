package com.azatdev.dailytasks.presentation.config.presentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azatdev.dailytasks.data.repositories.data.UsersRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsService;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsServiceImpl;

@Configuration
public class UsersConfig {
    @Bean
    public UsersRepository usersRepository(JpaUsersRepository jpaUsersRepository) {
        return new UsersRepositoryImpl(jpaUsersRepository);
    }

    @Bean
    public CustomUserDetailsService userDetailsService(UsersRepository usersRepository) {
        return new CustomUserDetailsServiceImpl(usersRepository);
    }
}
