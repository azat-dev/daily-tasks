package com.azatdev.dailytasks.domain.usecases.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.utils.Result;

class CustomUserDetailsServiceTests {

    private record SUT(
        UserDetailsService userDetailsService,
        UsersRepository usersRepository
    ) {
    }

    private SUT createSUT() {
        UsersRepository usersRepository = mock(UsersRepository.class);
        UserDetailsService userDetailsService = new CustomUserDetailsService(usersRepository);

        return new SUT(
            userDetailsService,
            usersRepository
        );
    }

    @Test
    void loadUserByUsernameNotExistingUserMustThrowAnExceptionTest() {

        // Given
        String wrongUserName = "wrongUserName";

        SUT sut = createSUT();
        given(sut.usersRepository.findByUsername(wrongUserName)).willReturn(Result.success(Optional.empty()));

        // Then

        then(sut.usersRepository).should(times(1))
            .findByUsername(wrongUserName);

        assertThrows(
            UsernameNotFoundException.class,
            () -> {
                sut.userDetailsService.loadUserByUsername(wrongUserName);
            }
        );
    }
}
