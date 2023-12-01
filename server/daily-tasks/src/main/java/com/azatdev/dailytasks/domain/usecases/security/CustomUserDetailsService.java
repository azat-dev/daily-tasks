package com.azatdev.dailytasks.domain.usecases.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepositoryFindByUserName;

public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepositoryFindByUserName usersRepository;

    public CustomUserDetailsService(UsersRepositoryFindByUserName usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final var result = usersRepository.findByUsername(username);

        if (!result.isSuccess()) {
            throw new IllegalStateException("Internal error occurred");
        }

        final var appUser = result.getValue()
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(username)
            .password(appUser.password())
            .build();
    }
}
