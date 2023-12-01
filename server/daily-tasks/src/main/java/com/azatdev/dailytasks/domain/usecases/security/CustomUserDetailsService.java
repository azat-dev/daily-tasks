package com.azatdev.dailytasks.domain.usecases.security;

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
        return null;
    }
}
