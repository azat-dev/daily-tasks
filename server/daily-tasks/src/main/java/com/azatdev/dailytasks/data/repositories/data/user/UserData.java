package com.azatdev.dailytasks.data.repositories.data.user;

import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public record UserData(
    @Id UUID id,
    @Nonnull String username,
    @Nonnull String password
) {

}
