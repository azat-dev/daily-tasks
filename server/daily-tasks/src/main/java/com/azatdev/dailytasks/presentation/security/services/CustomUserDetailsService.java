package com.azatdev.dailytasks.presentation.security.services;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.azatdev.dailytasks.presentation.security.entities.UserPrincipal;
import com.azatdev.dailytasks.presentation.security.services.jwt.UserIdNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {

    // Types

    public class UserNotFoundException extends Exception {
    }

    // Methods

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException;

    public UserPrincipal loadUserById(UUID userId) throws UserIdNotFoundException;
}
