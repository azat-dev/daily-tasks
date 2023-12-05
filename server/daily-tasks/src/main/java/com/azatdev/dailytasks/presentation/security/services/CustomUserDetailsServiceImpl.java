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

        final var appUserResult = usersRepository.findByUsername(username);

        return appUserResult.map((appUser) -> UserPrincipal.from(appUser))
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserPrincipal loadUserById(UUID userId) throws UserIdNotFoundException {

        final var appUserResult = usersRepository.findById(userId);

        return appUserResult.map((appUser) -> UserPrincipal.from(appUser))
            .orElseThrow(() -> new UserIdNotFoundException("User not found"));
    }
}
