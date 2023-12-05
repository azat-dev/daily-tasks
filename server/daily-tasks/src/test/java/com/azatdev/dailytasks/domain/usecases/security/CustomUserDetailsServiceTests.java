package com.azatdev.dailytasks.domain.usecases.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.domain.usecases.TestDomainDataGenerator;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsService;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsServiceImpl;
import com.azatdev.dailytasks.presentation.security.services.jwt.UserIdNotFoundException;

class CustomUserDetailsServiceTests {

    private record SUT(
        CustomUserDetailsService userDetailsService,
        UsersRepository usersRepository
    ) {
    }

    private SUT createSUT() {
        UsersRepository usersRepository = mock(UsersRepository.class);
        CustomUserDetailsService userDetailsService = new CustomUserDetailsServiceImpl(usersRepository);

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
        given(sut.usersRepository.findByUsername(wrongUserName)).willReturn(Optional.empty());

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

        given(sut.usersRepository.findByUsername(userName)).willReturn(Optional.of(user));

        // When
        final var result = sut.userDetailsService.loadUserByUsername(userName);

        // Then
        then(sut.usersRepository).should(times(1))
            .findByUsername(userName);

        assertThat(result).isNotNull();

        assertThat(result.getId()).isEqualTo(user.id());
        assertThat(result.getUsername()).isEqualTo(userName);
        assertThat(result.getPassword()).isEqualTo(user.password());
    }

    @Test
    void loadUserByIdNotExistingUserMustThrowAnExceptionTest() {

        // Given
        final var wrongUserId = UUID.randomUUID();

        SUT sut = createSUT();
        given(sut.usersRepository.findById(wrongUserId)).willReturn(Optional.empty());

        // When
        assertThrows(
            UserIdNotFoundException.class,
            () -> {
                sut.userDetailsService.loadUserById(wrongUserId);
            }
        );

        // Then
        then(sut.usersRepository).should(times(1))
            .findById(wrongUserId);
    }

    @Test
    void loadUserByIdExistingUserMustReturnUserDetailsTest() {

        // Given
        final var userName = "userName";
        final var user = TestDomainDataGenerator.anyAppUserWithUserName(userName);
        final var userId = user.id();

        SUT sut = createSUT();

        given(sut.usersRepository.findById(userId)).willReturn(Optional.of(user));

        // When
        final var result = sut.userDetailsService.loadUserById(userId);

        // Then
        then(sut.usersRepository).should(times(1))
            .findById(userId);

        assertThat(result).isNotNull();

        assertThat(result.getId()).isEqualTo(user.id());
        assertThat(result.getUsername()).isEqualTo(userName);
        assertThat(result.getPassword()).isEqualTo(user.password());
    }
}
