package com.azatdev.dailytasks.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepositoryCreate;
import com.azatdev.dailytasks.domain.models.AppUser;

public class SignUpAppUserUseCaseImplTest {

    private record SUT(
        SignUpAppUserUseCase useCase,
        UsersRepositoryCreate usersRepository
    ) {
    }

    private SUT createSUT() {
        final var usersRepository = mock(UsersRepositoryCreate.class);

        return new SUT(
            new SignUpAppUserUseCaseImpl(usersRepository),
            usersRepository
        );
    }

    @Test
    public void execute_givenNoExistingUserName_thenCreateUser() throws Exception {

        // Given
        final var username = "username";
        final var encodedPassword = "encodedPassword";

        final var sut = createSUT();

        final var userId = UUID.randomUUID();

        final var expectedAppUser = new AppUser(
            userId,
            username,
            encodedPassword
        );

        given(
            sut.usersRepository.create(
                username,
                encodedPassword
            )
        ).willReturn(expectedAppUser);

        // When
        final var createdUser = sut.useCase.execute(
            username,
            encodedPassword
        );

        // Then
        then(sut.usersRepository).should(times(1))
            .create(
                username,
                encodedPassword
            );

        assertThat(createdUser).isEqualTo(expectedAppUser);
    }

    @Test
    public void execute_givenExistingUserName_thenThrowException() throws Exception {

        // Given
        final var username = "username";
        final var encodedPassword = "encodedPassword";

        final var sut = createSUT();

        given(
            sut.usersRepository.create(
                username,
                encodedPassword
            )
        ).willThrow(new UsersRepositoryCreate.UsernameAlreadyExistsException());

        // When
        final var exception = assertThrows(
            SignUpAppUserUseCase.UsernameAlreadyExistsException.class,
            () -> sut.useCase.execute(
                username,
                encodedPassword
            )
        );

        // Then
        then(sut.usersRepository).should(times(1))
            .create(
                username,
                encodedPassword
            );

        assertThat(exception).isNotNull();
    }

    @Test
    public void execute_givenEmptyPassword_thenThrowException() throws Exception {

        // Given
        final var username = "username";
        final var emptyPassword = "";

        final var sut = createSUT();

        // When
        final var exception = assertThrows(
            SignUpAppUserUseCase.PasswordIsEmptyException.class,
            () -> sut.useCase.execute(
                username,
                emptyPassword
            )
        );

        // Then
        then(sut.usersRepository).should(never())
            .create(
                username,
                emptyPassword
            );

        assertThat(exception).isNotNull();
    }
}
