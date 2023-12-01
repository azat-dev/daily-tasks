package com.azatdev.dailytasks.domain.usecases.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepositoryFindByUserName;
import com.azatdev.dailytasks.domain.usecases.TestDomainDataGenerator;
import com.azatdev.dailytasks.utils.Result;

class CustomUserDetailsServiceTests {

    private record SUT(
        UserDetailsService userDetailsService,
        UsersRepositoryFindByUserName usersRepository
    ) {
    }

    private SUT createSUT() {
        UsersRepositoryFindByUserName usersRepository = mock(UsersRepositoryFindByUserName.class);
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

        // When
        assertThrows(
            UsernameNotFoundException.class,
            () -> {
                sut.userDetailsService.loadUserByUsername(wrongUserName);
            }
        );

        // Then
        then(sut.usersRepository).should(times(1))
            .findByUsername(wrongUserName);
    }

    @Test
    void loadUserByUsernameExistingUserMustReturnUserDetailsTest() {

        // Given
        final String userName = "userName";

        final var user = TestDomainDataGenerator.anyAppUserWithUserName(userName);

        SUT sut = createSUT();

        given(sut.usersRepository.findByUsername(userName)).willReturn(Result.success(Optional.of(user)));

        // When
        final var result = sut.userDetailsService.loadUserByUsername(userName);

        // Then
        then(sut.usersRepository).should(times(1))
            .findByUsername(userName);

        assertThat(result).isNotNull();

        UserDetails expectedUserDetails = User.withUsername(userName)
            .password(user.password())
            .build();

        assertThat(result).isEqualTo(expectedUserDetails);
    }
}
