package com.azatdev.dailytasks.presentation.security.services;

import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;
import com.azatdev.dailytasks.presentation.security.services.jwt.UserIdNotFoundException;

public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UsersRepository usersRepository;

    public CustomUserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {

        final var result = usersRepository.findByUsername(username);

        if (!result.isSuccess()) {
            throw new IllegalStateException("Internal error occurred");
        }

        final var appUser = result.getValue()
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserPrincipal.from(appUser);
    }

    public UserPrincipal loadUserById(UUID userId) throws UserIdNotFoundException {

        final var result = usersRepository.findById(userId);

        if (!result.isSuccess()) {
            throw new IllegalStateException("Internal error occurred");
        }

        final var appUser = result.getValue()
            .orElseThrow(() -> new UserIdNotFoundException("User not found"));

        return UserPrincipal.from(appUser);
    }
}
